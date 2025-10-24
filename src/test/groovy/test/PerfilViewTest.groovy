package test

import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*
import view.PerfilView

class PerfilViewTest {

    private InputStream streamOriginal

    @BeforeEach
    void guardarStreamDoTeclado() {
        streamOriginal = System.in
    }

    @AfterEach
    void voltarStreamDoTeclado() {
        System.setIn(streamOriginal)
    }

    @Test
    void deveRetornarOpcaoDigitadaPeloUsuario(){

        String inputSimulado = "5\n"
        ByteArrayInputStream inputStreamSimulado = new ByteArrayInputStream(inputSimulado.bytes)
        System.setIn(inputStreamSimulado)
        PerfilView perfilView = new PerfilView()
        int opcao = perfilView.escolherOpcao()
        println(opcao)
        assertEquals(5, opcao)
    }

    @Test
    void deveRetornarStringDigitadaPelousuario(){

        String inputSimulado = "Gabriel\n"
        ByteArrayInputStream inputStreamSimulado = new ByteArrayInputStream(inputSimulado.bytes)
        System.setIn(inputStreamSimulado)
        PerfilView perfilView = new PerfilView()
        String nome = perfilView.pedirStringAoUsuario("Me passe seu nome:")
        println(nome)
        assertEquals("Gabriel", nome)
    }
}
