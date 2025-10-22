package repository

import model.Candidato
import model.Empresa
import model.Vaga
import java.text.SimpleDateFormat


class Repository implements IRepository {

    private final List<Candidato> candidatos = []
    private final List<Empresa> empresas = []
    private final List<Vaga> vagas = []

    Repository() {
        inicializarDados()
    }

    private void inicializarDados() {
        def formatador = new SimpleDateFormat("yyyy-MM-dd")

        candidatos.add(new Candidato(1, "Gabriel", "gabriel@email.com", "Dev Java", "60000-000", "Brasil", "senha1", ["Java", "SQL"], "123.456.789-00", formatador.parse("2003-05-10"), "Silva"))
        candidatos.add(new Candidato(2, "Maria", "maria@email.com", "Front-end", "60001-000", "Brasil", "senha2", ["HTML", "CSS", "JavaScript"], "987.654.321-00", formatador.parse("2001-08-15"), "Oliveira"))
        candidatos.add(new Candidato(3, "João", "joao@email.com", "Full Stack", "60002-000", "Brasil", "senha3", ["Java", "React", "SQL"], "111.222.333-44", formatador.parse("1999-11-20"), "Santos"))
        candidatos.add(new Candidato(4, "Ana", "ana@email.com", "Mobile Developer", "60003-000", "Brasil", "senha4", ["Kotlin", "Flutter"], "555.666.777-88", formatador.parse("2002-02-25"), "Costa"))
        candidatos.add(new Candidato(5, "Carlos", "carlos@email.com", "Data Scientist", "60004-000", "Brasil", "senha5", ["Python", "R", "SQL"], "999.888.777-66", formatador.parse("2000-07-30"), "Pereira"))

        empresas.add(new Empresa(1, "TechCorp", "contato@techcorp.com", "Empresa de tecnologia", "60000-001", "Brasil", "empresa1", ["Java", "SQL"], "12.345.678/0001-00"))
        empresas.add(new Empresa(2, "WebSolutions", "contato@websolutions.com", "Desenvolvimento web", "60001-001", "Brasil", "empresa2", ["HTML", "CSS", "JavaScript"], "98.765.432/0001-11"))
        empresas.add(new Empresa(3, "DataLabs", "contato@datalabs.com", "Ciência de dados", "60002-001", "Brasil", "empresa3", ["Python", "R", "SQL"], "11.223.344/0001-22"))
        empresas.add(new Empresa(4, "MobileApps", "contato@mobileapps.com", "Desenvolvimento mobile", "60003-001", "Brasil", "empresa4", ["Kotlin", "Flutter"], "55.667.788/0001-33"))
        empresas.add(new Empresa(5, "AI Solutions", "contato@aisolutions.com", "Inteligência artificial", "60004-001", "Brasil", "empresa5", ["Python", "Machine Learning"], "99.887.766/0001-44"))

        vagas.add(new Vaga(1, "Desenvolvedor Java Pleno", "Vaga para desenvolvedor com 3+ anos de experiência.", "Remoto", 1, ["Java", "Spring"]))
        vagas.add(new Vaga(2, "Engenheiro de Dados", "Vaga para engenheiro de dados.", "São Paulo - SP", 3, ["Python", "SQL", "Airflow"]))
    }

    @Override
    void adicionarCandidato(Candidato c) {
        candidatos.add(c)
    }

    @Override
    List<Candidato> listarCandidatos() {
        return candidatos
    }

    @Override
    Candidato buscarCandidato(int id) {
        return (id >= 0 && id < candidatos.size()) ? candidatos[id] : null
    }

    @Override
    void atualizarCandidato(int id, Candidato c) {
        if (id >= 0 && id < candidatos.size()) {
            candidatos[id] = c
        }
    }

    @Override
    void adicionarEmpresa(Empresa e) {
        empresas.add(e)
    }

    @Override
    List<Empresa> listarEmpresas() {
        return empresas
    }

    @Override
    Empresa buscarEmpresa(int id) {
        return (id >= 0 && id < empresas.size()) ? empresas[id] : null
    }

    @Override
    void atualizarEmpresa(int id, Empresa e) {
        if (id >= 0 && id < empresas.size()) {
            empresas[id] = e
        }
    }

    @Override
    void adicionarVaga(Vaga vaga) {
        vagas << vaga
    }

    @Override
    List<Vaga> listarVagasPorEmpresa(int idEmpresa) {
        return vagas.findAll { it.idEmpresa == idEmpresa }
    }

    @Override
    List<Vaga> listarTodasVagas() {
        return vagas
    }
}