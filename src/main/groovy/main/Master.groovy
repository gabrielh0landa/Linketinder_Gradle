import controller.PerfilController
import repository.IRepository
import repository.RepositoryJdbc
import service.PerfilService
import view.CandidatoView
import view.EmpresaView

class Master {

    private static final CandidatoView menuView = new CandidatoView()

    static void main(String[] args) {
        println "Iniciando aplicação LinkeTinder..."

        IRepository repository = new RepositoryJdbc()
        PerfilService perfilService = new PerfilService(repository)
        CandidatoView candidatoView = new CandidatoView()
        EmpresaView empresaView = new EmpresaView()
        PerfilController perfilController = new PerfilController(perfilService, candidatoView, empresaView)

        int opcao = -1
        while (opcao != 0) {
            mostrarMenuPrincipal()
            opcao = menuView.pedirIntAoUsuario("Digite sua opção:")

            switch (opcao) {
                case 1:
                    perfilController.criarCandidato()
                    break
                case 2:
                    perfilController.atualizarCandidato()
                    break
                case 3:
                    perfilController.listarCandidatos()
                    break
                case 4:
                    perfilController.criarEmpresa()
                    break
                case 5:
                    perfilController.atualizarEmpresa()
                    break
                case 6:
                    perfilController.listarEmpresas()
                    break
                case 7:
                    perfilController.gerenciarCompetenciasCandidato()
                    break
                case 8:
                    perfilController.deletarPerfis()
                    break
                case 0:
                    println "Encerrando aplicação..."
                    break
                default:
                    println "Opção inválida!"
            }

            if (opcao != 0) {
                println "\nPressione Enter para continuar..."
                menuView.pedirStringAoUsuario("")
                println "\n----------------------------------\n"
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        println "------ Menu Principal (Master) -------"
        println "1. Criar Candidato"
        println "2. Atualizar Candidato (Substituir tudo)"
        println "3. Listar Candidatos"
        println "4. Criar Empresa"
        println "5. Atualizar Empresa (Substituir tudo)"
        println "6. Listar Empresas"
        println "7. Gerenciar Competências (Candidato)"
        println "8. Deletar Perfis"
        println "0. Sair"
    }
}