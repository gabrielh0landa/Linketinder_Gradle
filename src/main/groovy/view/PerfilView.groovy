package view

class PerfilView {
    private final Scanner scanner = new Scanner(System.in)

    int escolherOpcao() {
        println "------Menu-------"
        println "Escolha sua opção \n" +
                "1. Criar Perfil de Candidato\n" +
                "2. Atualizar Perfil de Candidato\n" +
                "3. Listar Candidatos\n" +
                "4. Criar Perfil de Empresa\n" +
                "5. Atualizar Perfil de Empresa\n" +
                "6. Listar Empresas\n" +
                "7. Criar Vaga\n" +
                "8. Listar todas as Vagas\n" +
                "9. Listar Vagas por Empresa\n" +
                "0. Sair"

        while (true){
            String entrada = scanner.nextLine()
            try {
                int opcao = Integer.parseInt(entrada.trim())
                return opcao
            }catch (NumberFormatException e){
                println "Opção Inválida"
            }
        }
    }

    String pedirStringAoUsuario(String mensagemDoPedido) {
        println "$mensagemDoPedido"
        String string = scanner.nextLine()
        return string
    }


    static void main(String[] args) {
        def pv = new PerfilView()
        int resultado = pv.escolherOpcao()
        println(resultado)

        String nome = pv.pedirStringAoUsuario("Me passe seu nome:")
        println "$nome"
    }
}
