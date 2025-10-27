package test

import org.junit.jupiter.api.*

import java.time.LocalDate

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

    @Test
    void deveRetornarIntDigitadaPeloUsuario(){
        String inputSimulado = "4\n"
        ByteArrayInputStream inputStreamSimulado = new ByteArrayInputStream(inputSimulado.bytes)
        System.setIn(inputStreamSimulado)
        PerfilView perfilView = new PerfilView()
        Integer idade = perfilView.pedirIntAoUsuario("Me passe sua idade")
        println(idade)
        assertEquals(4, idade)

    }

    @Test
    void deveRetornarLocalDateDigitadaPeloUsuario(){
        String inputSimulado = "23/10/2005\n"
        ByteArrayInputStream inputStreamSimulado = new ByteArrayInputStream(inputSimulado.bytes)
        System.setIn(inputStreamSimulado)
        PerfilView perfilView = new PerfilView()
        LocalDate dataDeNascimento = perfilView.pedirDataAoUsuario("Me passe sua data de nascimneto")
        println(dataDeNascimento)
        LocalDate dataEsperada = LocalDate.of(2005, 10, 23)
        assertEquals(dataEsperada, dataDeNascimento)
    }

}
