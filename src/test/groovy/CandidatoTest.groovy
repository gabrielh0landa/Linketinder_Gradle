package test

import model.Candidato
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.Date
import static org.junit.jupiter.api.Assertions.*

class CandidatoTest {
    SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd")

    @Test
    void "construtorComAtributos"() {
        int id = 1
        String nome = "Ana Clara"
        String email = "ana.clara@email.com"
        String descricao = "Desenvolvedora backend com experiência em Java e Groovy"
        String cep = "61800-000"
        String pais = "Brasil"
        String senha = "123"
        List<String> competencias = ["Java", "Groovy", "SQL"]
        String cpf = "111.222.333-44"
        String data = "2005-10-23"
        Date data_nascimento = formatador.parse(data)
        String sobrenome = "Holanda"

        Candidato candidato = new Candidato(id, nome, email, descricao, cep, pais, senha, competencias, cpf, data_nascimento, sobrenome)

        assertNotNull(candidato)
        assertEquals(id, candidato.id)
        assertEquals(nome, candidato.nome)
        assertEquals(email, candidato.email)
        assertEquals(descricao, candidato.descricao)
        assertEquals(cep, candidato.cep)
        assertEquals(sobrenome, candidato.sobrenome)
        assertEquals(pais, candidato.pais)
        assertEquals(competencias, candidato.competencias)
        assertEquals(cpf, candidato.cpf)
        assertEquals(data_nascimento, candidato.data_nascimento)
    }

    @Test
    void "construtorVazio"() {
        Candidato candidato = new Candidato()

        assertNotNull(candidato)
        assertNull(candidato.nome)
        assertNull(candidato.cpf)
        assertNull(candidato.data_nascimento)
    }
}