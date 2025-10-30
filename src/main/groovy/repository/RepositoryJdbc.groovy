package repository

import model.Candidato
import model.Empresa
import model.Vaga

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

                def novoId = generatedKeys[0][0]
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
                        dataDeNascimento: row.data_nascimento.toLocalDate(),
                        email: row.email, cpf: row.cpf,
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
                        dataDeNascimento: row.data_nascimento.toLocalDate(),
                        email: row.email, cpf: row.cpf,
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
    void deletarCandidato(int id) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            sql.withTransaction {
                // 1. Limpar dependências (tabela de junção)
                println "[Repo] Deletando competências do candidato $id..."
                sql.execute("DELETE FROM candidato_competencia WHERE id_candidato = ?;", [id])

                // 2. Limpar outras dependências (ex: candidatura)
                println "[Repo] Deletando candidaturas do candidato $id..."
                sql.execute("DELETE FROM candidatura WHERE id_candidato = ?;", [id])

                // 3. Deletar o candidato principal
                println "[Repo] Deletando candidato $id..."
                def rowsAffected = sql.executeUpdate("DELETE FROM candidato WHERE id = ?;", [id])

                if (rowsAffected == 0) {
                    println "[Repo] Aviso: Ninguém foi deletado. Candidato com ID $id não encontrado."
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
                def empresaDaVaga = vaga.empresa
                def idEmpresaDaVaga = empresaDaVaga.id
                def generatedKeys = sql.executeInsert(query, [vaga.nome, vaga.descricao, vaga.local, idEmpresaDaVaga], ['id'])
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

            Empresa empresaDonaDasVagas = _buscarEmpresaSemVagas(sql, idEmpresa)
            if (empresaDonaDasVagas == null) {
                println "[Repo] Aviso: Empresa com ID $idEmpresa não encontrada. Retornando lista de vagas vazia."
                return vagas
            }
            String query = "SELECT * FROM vaga WHERE id_empresa = ? ORDER BY id;"
            sql.eachRow(query, [idEmpresa]) { row ->
                def vaga = new Vaga(row.id, row.nome, row.descricao, row.local, empresaDonaDasVagas, [])
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

            Map<Integer, Empresa> empresasEncontradas = [:]

            sql.eachRow(query) { row ->
                Integer idEmpresa = row.id_empresa
                Empresa empresaDaVaga = null

                if (idEmpresa != null) {
                    if (!empresasEncontradas.containsKey(idEmpresa)) {
                        empresasEncontradas[idEmpresa] = _buscarEmpresaSemVagas(sql, idEmpresa)
                    }
                    empresaDaVaga = empresasEncontradas[idEmpresa]
                }
                def vaga = new Vaga(row.id, row.nome, row.descricao, row.local, empresaDaVaga, [])

                vaga.competencias = findCompetenciasForVaga(vaga.id)

                vagas.add(vaga)
            }
        } finally {
            sql.close()
        }
        return vagas
    }

    @Override
    void deletarEmpresa(int id) {
        def sql = DatabaseConfig.getSqlInstance()
        try {
            sql.withTransaction {
                // 1. Pegar todos os IDs de vagas da empresa
                // (Usamos uma nova instância sql aqui porque a transação principal pode interferir
                // ou podemos simplesmente usar a mesma instância sql da transação)
                def vagaIds = sql.rows("SELECT id FROM vaga WHERE id_empresa = ?;", [id]).collect { it.id }

                if (vagaIds) {
                    println "[Repo] Empresa $id tem vagas. Deletando dependências das vagas..."
                    // 2. Limpar dependências das vagas (vaga_competencia)
                    String inClauseVagas = vagaIds.collect { '?' }.join(', ')
                    sql.execute("DELETE FROM vaga_competencia WHERE id_vaga IN ($inClauseVagas);", vagaIds)

                    // 3. Limpar dependências das vagas (candidatura)
                    sql.execute("DELETE FROM candidatura WHERE id_vaga IN ($inClauseVagas);", vagaIds)

                    // 4. Deletar as vagas
                    println "[Repo] Deletando vagas da empresa $id..."
                    sql.execute("DELETE FROM vaga WHERE id_empresa = ?;", [id])
                }

                // 5. Limpar outras dependências da empresa (ex: interesses_empresa)
                println "[Repo] Deletando interesses da empresa $id..."
                sql.execute("DELETE FROM interesses_empresa WHERE id_empresa = ?;", [id])

                // 6. Deletar a empresa principal
                println "[Repo] Deletando empresa $id..."
                def rowsAffected = sql.executeUpdate("DELETE FROM empresa WHERE id = ?;", [id])
                if (rowsAffected == 0) {
                    println "[Repo] Aviso: Ninguém foi deletado. Empresa com ID $id não encontrada."
                }
            }
        } finally {
            sql.close()
        }
    }

    private Empresa _buscarEmpresaSemVagas(def sql, int id) {
        try {
            String query = "SELECT * FROM empresa WHERE id = ?;"
            def row = sql.firstRow(query, [id])
            if (row) {
                return new Empresa(
                        id: row.id, nome: row.nome, email: row.email, cnpj: row.cnpj,
                        pais: row.pais, cep: row.cep, descricao: row.descricao, senha: row.senha
                )
            }
        } catch (Exception e) {
            println "Erro ao buscar empresa (ID: $id) sem vagas: ${e.message}"
        }
        return null
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