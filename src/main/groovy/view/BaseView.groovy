package view

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

abstract class BaseView {

    protected final Scanner scanner = new Scanner(System.in)

    protected String pedirStringAoUsuario(String mensagemDoPedido) {
        println "$mensagemDoPedido"
        String string = scanner.nextLine()
        return string
    }

    public int pedirIntAoUsuario(String mensagemDoPedido) {
        println "$mensagemDoPedido"

        while (true) {
            String entrada = scanner.nextLine()
            try {
                Integer inteiro = Integer.parseInt(entrada.trim())
                return inteiro
            } catch (NumberFormatException e) {
                println "Opção Inválida"
                e.printStackTrace()
            }
        }
    }

    protected LocalDate pedirDataAoUsuario(String mensagemDoPedido) {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        println "$mensagemDoPedido (use o formato dd/mm/aaaa):"

        while (true) {
            String entrada = scanner.nextLine()
            try {
                LocalDate data = LocalDate.parse(entrada.trim(), formatador)
                return data
            } catch (DateTimeParseException e) {
                println "Data Inválida! Por favor, digite no formato dd/mm/aaaa."
                e.printStackTrace()
            }
        }
    }

    protected String pedirStringAoUsuarioComValorPadrao(String mensagem, String valorPadrao) {
        println "$mensagem - valor atual: $valorPadrao"
        println "Digite o novo valor ou se desejar manter o valor aperte 'enter'"

        String novoValor = scanner.nextLine()
        if (novoValor.trim().isEmpty()) {
            return valorPadrao
        } else {
            return novoValor.trim()
        }
    }

    protected int pedirIntAoUsuarioComValorPadrao(String mensagem, int valorPadrao) {
        println "$mensagem - valor atual: $valorPadrao"
        println "Digite o novo valor ou se desejar manter o valor aperte 'enter'"

        while (true) {
            String novoValor = scanner.nextLine()
            if (novoValor.trim().isEmpty()) {
                return valorPadrao
            }
            try {
                Integer inteiro = Integer.parseInt(novoValor.trim())
                return inteiro
            } catch (NumberFormatException e) {
                println "Opção Inválida. Digite um número ou pressione Enter."
                e.printStackTrace()
            }
        }
    }

    protected LocalDate pedirDataAoUsuarioComValorPadrao(String mensagem, LocalDate valorPadrao) {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        String dataPadraoFormatada = valorPadrao.format(formatador)

        println "$mensagem (atual: $dataPadraoFormatada)"
        println "Novo valor (formato dd/mm/aaaa ou Enter para manter):"

        while (true) {
            String novoValor = scanner.nextLine()
            if (novoValor.trim().isEmpty()) {
                return valorPadrao
            }
            try {
                LocalDate data = LocalDate.parse(novoValor.trim(), formatador)
                return data
            } catch (DateTimeParseException e) {
                println "Data Inválível! Digite no formato dd/mm/aaaa ou pressione Enter."
                e.printStackTrace()
            }
        }
    }
}