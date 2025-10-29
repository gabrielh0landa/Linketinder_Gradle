package repository

import model.Candidato
import model.Empresa
import model.Vaga
import java.sql.Date as SqlDate

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
                def dataParaBanco = new SqlDate(candidato.data_nascimento.getTime())

                def generatedKeys = sql.executeInsert(insertQuery, [
                        candidato.nome, candidato.sobrenome, dataParaBanco,
                        candidato.email, candidato.cpf, candidato.pais,
                        candidato.cep, candidato.descricao, candidato.senha
                ], ['id'])

                def novoId = generatedKeys[0][0]
                candidato.id = novoId
                candidato.competencias.each { nomeCompetencia ->
                    def competenciaId = findOrCreateCompetencia(sql, nomeCompetencia)
                    sql.execute("INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (?, ?);", [novoId, competenciaId])
                }
            }
                if (generatedKeys) {
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
                        data_nascimento: row.data_nascimento, email: row.email, cpf: row.cpf,
                        pais: row.pais, cep: row.cep, descricao: row.descricao, senha: row.senha
                )
                candidato.competencias = findCompetenciasForCandidato(candidato.id)
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
                        data_nascimento: row.data_nascimento, email: row.email, cpf: row.cpf,
                        pais: row.pais, cep: row.cep, descricao: row.descricao, senha: row.senha
                )
                candidato.competencias = findCompetenciasForCandidato(candidato.id)
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
                def dataParaBanco = new SqlDate(candidato.data_nascimento.getTime())

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
                empresas << new Empresa(
                        id: row.id, nome: row.nome, email: row.email, cnpj: row.cnpj,
                        pais: row.pais, cep: row.cep, descricao: row.descricao, senha: row.senha
                )
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
            String query = "SELECT * FROM empresa WHERE id = ?;"
            def row = sql.firstRow(query, [id])
            if (row) {
                return new Empresa(
                        id: row.id, nome: row.nome, email: row.email, cnpj: row.cnpj,
                        pais: row.pais, cep: row.cep, descricao: row.descricao, senha: row.senha
                )
            }
        } finally {
            sql.close()
        }
        return null
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
                def generatedKeys = sql.executeInsert(query, [vaga.nome, vaga.descricao, vaga.local, vaga.idEmpresa], ['id'])
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
        List<Vaga> vagas = []
        try {
            String query = "SELECT * FROM vaga WHERE id_empresa = ? ORDER BY id;"
            sql.eachRow(query, [idEmpresa]) { row ->
                def vaga = new Vaga(row.id, row.nome, row.descricao, row.local, row.id_empresa, [])
                vaga.competencias = findCompetenciasForVaga(vaga.id)
                vagas.add(vaga)
            }
        } finally {
            sql.close()
        }
        return vagas
    }

    @Override
    List<Vaga> listarTodasVagas() {
        def sql = DatabaseConfig.getSqlInstance()
        List<Vaga> vagas = []
        try {
            String query = "SELECT * FROM vaga ORDER BY id;"
            sql.eachRow(query) { row ->
                def vaga = new Vaga(row.id, row.nome, row.descricao, row.local, row.id_empresa, [])
                vaga.competencias = findCompetenciasForVaga(vaga.id)
                vagas.add(vaga)
            }
        } finally {
            sql.close()
        }
        return vagas
    }

    private List<String> findCompetenciasForCandidato(int candidatoId) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            String query = "SELECT c.nome FROM competencia c JOIN candidato_competencia cc ON c.id = cc.id_competencia WHERE cc.id_candidato = ?"
            return sql.rows(query, [candidatoId]).collect { it.nome }
        } finally {
            sql.close()
        }
    }

    private List<String> findCompetenciasForVaga(int vagaId) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            String query = "SELECT c.nome FROM competencia c JOIN vaga_competencia vc ON c.id = vc.id_competencia WHERE vc.id_vaga = ?"
            return sql.rows(query, [vagaId]).collect { it.nome }
        } finally {
            sql.close()
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

}