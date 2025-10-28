package repository

import model.Candidato
import model.Empresa
import model.Vaga
import java.sql.Date as SqlDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RepositoryJdbc implements IRepository {

    @Override
    void adicionarCandidato(Candidato candidato) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            sql.withTransaction {
                String insertQuery = """
                    INSERT INTO candidato(nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                """
                def dataParaBanco = java.sql.Date.valueOf(candidato.dataDeNascimento)

                def generatedKeys = sql.executeInsert(insertQuery, [
                        candidato.nome, candidato.sobrenome, dataParaBanco,
                        candidato.email, candidato.cpf, candidato.pais,
                        candidato.cep, candidato.descricao, candidato.senha
                ], ['id'])

                Integer novoId = generatedKeys[0][0]
                candidato.id = novoId
                candidato.competencias.each { nomeCompetencia ->
                    def competenciaId = findOrCreateCompetencia(sql, nomeCompetencia)
                    sql.execute("INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (?, ?);", [novoId, competenciaId])
                }
            }

        } finally {
            sql.close()
        }
    }

    @Override
    List<Candidato> listarCandidatos() {
        def sql = DatabaseConfig.getSqlInstance()
        List<Candidato> candidatos = []
        try {
            String query = "SELECT * FROM candidato ORDER BY id;"
            sql.eachRow(query) { row ->
                def candidato = new Candidato(
                        id: row.id, nome: row.nome, sobrenome: row.sobrenome,
                        dataDeNascimento: row.data_nascimento.toLocalDate(), email: row.email, cpf: row.cpf,
                        pais: row.pais, cep: row.cep, descricao: row.descricao, senha: row.senha
                )
                candidato.competencias = findCompetenciasForCandidato(sql, candidato.id)
                candidatos.add(candidato)
            }
        } finally {
            sql.close()
        }
        return candidatos
    }

    @Override
    Candidato buscarCandidato(int id) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            String query = "SELECT * FROM candidato WHERE id = ?;"
            def row = sql.firstRow(query, [id])
            if (row) {
                def candidato = new Candidato(
                        id: row.id, nome: row.nome, sobrenome: row.sobrenome,
                        dataDeNascimento: row.data_nascimento.toLocalDate(), email: row.email, cpf: row.cpf,
                        pais: row.pais, cep: row.cep, descricao: row.descricao, senha: row.senha
                )
                candidato.competencias = findCompetenciasForCandidato(sql, candidato.id)
                return candidato
            }
        } finally {
            sql.close()
        }
        return null
    }

    @Override
    void atualizarCandidato(int id, Candidato candidato) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            sql.withTransaction {
                String updateQuery = "UPDATE candidato SET nome = ?, sobrenome = ?, data_nascimento = ?, email = ?, cpf = ?, pais = ?, cep = ?, descricao = ?, senha = ? WHERE id = ?;"
                def dataParaBanco = java.sql.Date.valueOf(candidato.dataDeNascimento)
                sql.executeUpdate(updateQuery, [
                        candidato.nome, candidato.sobrenome, dataParaBanco,
                        candidato.email, candidato.cpf, candidato.pais,
                        candidato.cep, candidato.descricao, candidato.senha, id
                ])

                sql.execute("DELETE FROM candidato_competencia WHERE id_candidato = ?;", [id])
                candidato.competencias.each { nomeCompetencia ->
                    def competenciaId = findOrCreateCompetencia(sql, nomeCompetencia)
                    if (competenciaId) {
                        sql.execute("INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (?, ?);", [id, competenciaId])
                    }
                }
            }
        } finally {
            sql.close()
        }
    }

    @Override
    void adicionarEmpresa(Empresa empresa) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            String query = "INSERT INTO empresa(nome, email, cnpj, pais, cep, descricao, senha) VALUES (?, ?, ?, ?, ?, ?, ?);"
            def generatedKeys = sql.executeInsert(query, [
                    empresa.nome, empresa.email, empresa.cnpj, empresa.pais,
                    empresa.cep, empresa.descricao, empresa.senha
            ], ['id'])
            if (generatedKeys) {
                empresa.id = generatedKeys[0][0]
            }
        } finally {
            sql.close()
        }
    }

    @Override
    List<Empresa> listarEmpresas() {
        def sql = DatabaseConfig.getSqlInstance()
        List<Empresa> empresas = []
        try {
            String query = "SELECT * FROM empresa ORDER BY id;"
            sql.eachRow(query) { row ->
                def empresa = _buildEmpresaFromRow(row)
                empresa.vagas = _listarVagasPorEmpresa(sql, empresa)
                empresas << empresa
            }
        } finally {
            sql.close()
        }
        return empresas
    }

    @Override
    Empresa buscarEmpresa(int id) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            Empresa empresa = _buscarEmpresaSemVagas(sql, id)
            if (empresa) {
                empresa.vagas = _listarVagasPorEmpresa(sql, empresa)
            }
            return empresa
        } finally {
            sql.close()
        }
    }

    @Override
    void atualizarEmpresa(int id, Empresa empresa) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            String query = "UPDATE empresa SET nome = ?, email = ?, cnpj = ?, pais = ?, cep = ?, descricao = ?, senha = ? WHERE id = ?;"
            sql.executeUpdate(query, [
                    empresa.nome, empresa.email, empresa.cnpj, empresa.pais,
                    empresa.cep, empresa.descricao, empresa.senha, id
            ])
        } finally {
            sql.close()
        }
    }

    @Override
    void adicionarVaga(Vaga vaga) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            sql.withTransaction {
                String query = "INSERT INTO vaga(nome, descricao, local, id_empresa) VALUES (?, ?, ?, ?);"

                def idEmpresa = vaga.empresa ? vaga.empresa.id : null

                def generatedKeys = sql.executeInsert(query, [vaga.nome, vaga.descricao, vaga.local, idEmpresa], ['id'])
                if (generatedKeys) {
                    def vagaId = generatedKeys[0][0]
                    vaga.id = vagaId
                    vaga.competencias.each { nomeCompetencia ->
                        def competenciaId = findOrCreateCompetencia(sql, nomeCompetencia)
                        sql.execute("INSERT INTO vaga_competencia (id_vaga, id_competencia) VALUES (?, ?);", [vagaId, competenciaId])
                    }
                }
            }
        } finally {
            sql.close()
        }
    }

    @Override
    List<Vaga> listarVagasPorEmpresa(int idEmpresa) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            Empresa empresa = _buscarEmpresaSemVagas(sql, idEmpresa)

            if (empresa) {
                return _listarVagasPorEmpresa(sql, empresa)
            }
            return []
        } finally {
            sql.close()
        }
    }

    @Override
    List<Vaga> listarTodasVagas() {
        def sql = DatabaseConfig.getSqlInstance()
        List<Vaga> vagas = []
        try {
            String query = "SELECT * FROM vaga ORDER BY id;"

            Map<Integer, Empresa> empresasEncontradas = [:]

            sql.eachRow(query) { row ->
                Integer idEmpresa = row.id_empresa

                if (!empresasEncontradas.containsKey(idEmpresa)) {
                    empresasEncontradas[idEmpresa] = _buscarEmpresaSemVagas(sql, idEmpresa)
                }
                Empresa empresaDaVaga = empresasEncontradas[idEmpresa]

                def vaga = new Vaga(row.id, row.nome, row.descricao, row.local, empresaDaVaga, [])

                vaga.competencias = findCompetenciasForVaga(sql, vaga.id)
                vagas.add(vaga)
            }
        } finally {
            sql.close()
        }
        return vagas
    }

    private Empresa _buildEmpresaFromRow(def row) {
        return new Empresa(
                id: row.id, nome: row.nome, email: row.email, cnpj: row.cnpj,
                pais: row.pais, cep: row.cep, descricao: row.descricao, senha: row.senha
        )
    }

    private List<Vaga> _listarVagasPorEmpresa(def sql, Empresa empresa) {
        List<Vaga> vagas = []
        String query = "SELECT * FROM vaga WHERE id_empresa = ? ORDER BY id;"

        sql.eachRow(query, [empresa.id]) { row ->
            def vaga = new Vaga(row.id, row.nome, row.descricao, row.local, empresa, [])

            vaga.competencias = findCompetenciasForVaga(sql, vaga.id)
            vagas.add(vaga)
        }
        return vagas
    }

    private Empresa _buscarEmpresaSemVagas(def sql, int id) {
        try {
            String query = "SELECT * FROM empresa WHERE id = ?;"
            def row = sql.firstRow(query, [id])
            if (row) {
                return _buildEmpresaFromRow(row)
            }
        } catch (Exception e) {
            e.printStackTrace()
            return null
        }
        return null
    }

    private List<String> findCompetenciasForCandidato(def sql, int candidatoId) {
        try {
            String query = "SELECT c.nome FROM competencia c JOIN candidato_competencia cc ON c.id = cc.id_competencia WHERE cc.id_candidato = ?"
            return sql.rows(query, [candidatoId]).collect { it.nome }
        } finally {
        }
    }

    private List<String> findCompetenciasForVaga(def sql, int vagaId) {
        try {
            String query = "SELECT c.nome FROM competencia c JOIN vaga_competencia vc ON c.id = vc.id_competencia WHERE vc.id_vaga = ?"
            return sql.rows(query, [vagaId]).collect { it.nome }
        } finally {
        }
    }

    private Integer findOrCreateCompetencia(def sql, String nomeCompetencia) {
        def row = sql.firstRow("SELECT id FROM competencia WHERE nome = ?;", [nomeCompetencia])
        if (row) {
            return row.id
        } else {
            def generatedKeys = sql.executeInsert("INSERT INTO competencia (nome) VALUES (?);", [nomeCompetencia], ['id'])
            return generatedKeys[0][0]
        }
    }

    static void main(String[] args) {

        try {
            println "--- Teste listarEmpresas ---"
            def rj = new RepositoryJdbc()
            List<Empresa> empresas = rj.listarEmpresas()
            empresas.each { println it }

        } catch (Exception e) {
            e.printStackTrace()
        }

    }
}