package test

import model.Empresa
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class EmpresaTest {

    @Test
    void "construtorComAtributos"() {
        Integer id = 1
        String nome = "Inova Software"
        String email = "contato@inovasoftware.com"
        String descricao = "Desenvolvedora de aplicativos mobile inovadores"
        String cep = "55.666-777"
        String pais = "Brasil"
        String senha = "123"
        List<String> competencias = ["Swift", "Kotlin"]
        String cnpj = "55.666.777/0001-88"

        Empresa empresa = new Empresa(id, nome, email, descricao, cep, pais, senha, competencias, cnpj)

        assertNotNull(empresa)
        assertEquals(id, empresa.id)
        assertEquals(nome, empresa.nome)
        assertEquals(email, empresa.email)
        assertEquals(descricao, empresa.descricao)
        assertEquals(cep, empresa.cep)
        assertEquals(pais, empresa.pais)
        assertEquals(competencias, empresa.competencias)
        assertEquals(cnpj, empresa.cnpj)
        assertEquals(senha, empresa.senha)
    }

    @Test
    void "construtorVazio"() {
        Empresa empresa = new Empresa()

        assertNotNull(empresa)
        assertNull(empresa.nome)
        assertNull(empresa.cnpj)
    }
}