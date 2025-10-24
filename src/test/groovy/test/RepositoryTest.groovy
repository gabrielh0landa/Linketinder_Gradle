package test

import model.Candidato
import model.Empresa
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.Repository
import java.text.SimpleDateFormat

import static org.junit.jupiter.api.Assertions.*

class RepositoryTest {

    private Repository repository
    private final SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd")

    @BeforeEach
    void setUp() {
        repository = new Repository()
    }

    @Test
    void "deve inicializar com 5 candidatos"() {
        List<Candidato> candidatos = repository.listarCandidatos()

        assertNotNull(candidatos)
        assertEquals(5, candidatos.size())
        assertEquals("Gabriel", candidatos[0].nome)
    }

    @Test
    void "deve adicionar um novo candidato"() {
        Date dataNascimento = formatador.parse("1995-04-12")
        Candidato novoCandidato = new Candidato(6, "Lucas", "lucas@email.com", "DBA", "60005-000", "Brasil", "senha6", ["Oracle", "PostgreSQL"], "444.555.666-77", dataNascimento, "Mendes")

        repository.adicionarCandidato(novoCandidato)
        List<Candidato> candidatos = repository.listarCandidatos()

        assertEquals(6, candidatos.size())
        assertEquals("Lucas", candidatos.last().nome)
    }

    @Test
    void "deve buscar um candidato por um índice válido e inválido"() {
        Candidato candidato = repository.buscarCandidato(1)

        assertNotNull(candidato)
        assertEquals("Maria", candidato.nome)

        Candidato candidatoInexistente = repository.buscarCandidato(99)
        assertNull(candidatoInexistente)
    }

    @Test
    void "deve atualizar um candidato existente"() {
        Date dataNascimento = formatador.parse("2003-05-10")
        Candidato candidatoAtualizado = new Candidato(1, "Gabriel", "gabriel.santos@email.com", "Dev Java Sênior", "60000-000", "Brasil", "novaSenha", ["Java", "SQL", "Spring"], "123.456.789-00", dataNascimento, "Santos")
        int indiceParaAtualizar = 0

        repository.atualizarCandidato(indiceParaAtualizar, candidatoAtualizado)
        Candidato candidatoBuscado = repository.buscarCandidato(indiceParaAtualizar)

        assertNotNull(candidatoBuscado)
        assertEquals("Gabriel", candidatoBuscado.nome)
        assertEquals("Santos", candidatoBuscado.sobrenome)
        assertEquals("Dev Java Sênior", candidatoBuscado.descricao)
    }

    @Test
    void "deve inicializar com 5 empresas"() {
        List<Empresa> empresas = repository.listarEmpresas()

        assertNotNull(empresas)
        assertEquals(5, empresas.size())
        assertEquals("TechCorp", empresas[0].nome)
    }

    @Test
    void "deve adicionar uma nova empresa"() {
        Empresa novaEmpresa = new Empresa(6, "CloudExperts", "contato@cloudexperts.com", "Consultoria em nuvem", "60005-001", "Brasil", "empresa6", ["AWS", "GCP"], "10.203.040/0001-50")

        repository.adicionarEmpresa(novaEmpresa)
        List<Empresa> empresas = repository.listarEmpresas()

        assertEquals(6, empresas.size())
        assertEquals("CloudExperts", empresas.last().nome)
    }

    @Test
    void "deve buscar uma empresa por um índice válido e inválido"() {
        Empresa empresa = repository.buscarEmpresa(2)

        assertNotNull(empresa)
        assertEquals("DataLabs", empresa.nome)

        Empresa empresaInexistente = repository.buscarEmpresa(10)
        assertNull(empresaInexistente)
    }

    @Test
    void "deve atualizar uma empresa existente"() {
        Empresa empresaAtualizada = new Empresa(1, "TechCorp Global", "contato@techcorpglobal.com", "Empresa de tecnologia global", "60000-001", "Brasil", "senhaAtualizada", ["Java", "SQL", "Cloud"], "12.345.678/0001-00")
        int indiceParaAtualizar = 0

        repository.atualizarEmpresa(indiceParaAtualizar, empresaAtualizada)
        Empresa empresaBuscada = repository.buscarEmpresa(indiceParaAtualizar)

        assertNotNull(empresaBuscada)
        assertEquals("TechCorp Global", empresaBuscada.nome)
        assertEquals("Empresa de tecnologia global", empresaBuscada.descricao)
    }
}