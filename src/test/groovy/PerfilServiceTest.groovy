package test

import model.Candidato
import model.Empresa
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.PerfilService
import java.text.SimpleDateFormat
import java.util.Date
import static org.junit.jupiter.api.Assertions.*

class PerfilServiceTest {

    private PerfilService perfilService
    private final SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd")

    @BeforeEach
    void setUp() {
        perfilService = new PerfilService()
    }

    @Test
    void "criarCandidatoTeste"() {
        String nome = "Carlos"
        String sobrenome = "Silva"
        String cpf = "123.456.789-00"
        String email = "carlos.silva@example.com"
        String descricao = "Engenheiro de Software"
        String cep = "12345-678"
        String pais = "Brasil"
        String senha = "senha123"
        List<String> competencias = ["Java", "Spring Boot"]
        String data = "1990-05-15"
        Date dataNascimento = formatador.parse(data)

        Candidato candidato = perfilService.criarNovoCandidato(nome, sobrenome, email, descricao, cep, pais, senha, competencias, cpf, dataNascimento)

        assertNotNull(candidato)
        assertNull(candidato.id)
        assertEquals(nome, candidato.nome)
        assertEquals(sobrenome, candidato.sobrenome)
        assertEquals(cpf, candidato.cpf)
    }

    @Test
    void "atualizarCandidatoTeste"() {
        Date dataNascimentoAntiga = formatador.parse("2000-01-10")
        Candidato candidato = new Candidato(2, "Ana", "ana@antiga.com", "Dev Jr", "11111-111", "Brasil", "senha456", ["HTML"], "111.111.111-11", dataNascimentoAntiga, "Antiga")

        String novoNome = "Ana Clara"
        String novoSobrenome = "Nova"
        String novoCpf = "222.222.222-22"
        Date novaDataNascimento = formatador.parse("1998-02-20")
        String novoEmail = "ana.nova@email.com"
        String novaDescricao = "Desenvolvedora Pleno"
        String novoCep = "22222-222"
        String novoPais = "Portugal"
        List<String> novasCompetencias = ["CSS", "JavaScript"]

        perfilService.atualizarPerfilCandidato(candidato, novoNome, novoSobrenome, novoCpf, novaDataNascimento, novoEmail, novaDescricao, novoCep, novoPais, novasCompetencias)

        assertEquals(novoNome, candidato.nome)
        assertEquals(novoSobrenome, candidato.sobrenome)
        assertEquals(novoCpf, candidato.cpf)
        assertEquals(novaDataNascimento, candidato.data_nascimento)
        assertEquals(novoEmail, candidato.email)
        assertEquals(novaDescricao, candidato.descricao)
        assertEquals(novoCep, candidato.cep)
        assertEquals(novoPais, candidato.pais)
        assertTrue(candidato.competencias.containsAll(["HTML", "CSS", "JavaScript"]))
    }

    @Test
    void "criarEmpresaTeste"() {
        String nome = "Tech Solutions Ltda"
        String cnpj = "12.345.678/0001-99"
        String email = "contato@techsolutions.com"
        String descricao = "Soluções em nuvem"
        String cep = "87654-321"
        String pais = "Brasil"
        String senha = "abc"
        List<String> competencias = ["AWS", "Azure"]

        Empresa empresa = perfilService.criarNovaEmpresa(nome, cnpj, email, descricao, cep, pais, senha, competencias)

        assertNotNull(empresa)
        assertNull(empresa.id)
        assertEquals(nome, empresa.nome)
        assertEquals(cnpj, empresa.cnpj)
    }

    @Test
    void "atualizarEmpresaTeste"() {
        Empresa empresa = new Empresa(4, "Firma Velha", "contato@velha.com", "Vendas", "99999-999", "Brasil", "xyz", ["Vendas"], "99.999.999/0001-99")

        String novoNome = "Firma Nova & Cia"
        String novoCnpj = "88.888.888/0001-88"
        String novoEmail = "contato@firmanova.com"
        String novaDescricao = "Marketing Digital e Vendas Globais"
        String novoCep = "88888-888"
        String novoPais = "Canadá"
        List<String> novasCompetencias = ["Marketing", "SEO"]

        perfilService.atualizarPerfilEmpresa(empresa, novoNome, novoCnpj, novoEmail, novaDescricao, novoCep, novoPais, novasCompetencias)

        assertEquals(novoNome, empresa.nome)
        assertEquals(novoCnpj, empresa.cnpj)
        assertEquals(novoEmail, empresa.email)
        assertEquals(novaDescricao, empresa.descricao)
        assertEquals(novoCep, empresa.cep)
        assertEquals(novoPais, empresa.pais)
        assertTrue(empresa.competencias.containsAll(["Vendas", "Marketing", "SEO"]))
    }
}