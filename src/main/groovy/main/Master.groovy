import controller.PerfilController
import repository.IRepository
import repository.RepositoryJdbc
import service.PerfilService
import view.CandidatoView
import view.EmpresaView
import view.BaseView // Precisamos de uma view com os helpers genéricos para o menu

/**
 * Classe principal da aplicação (Simulação).
 * Responsável por:
 * 1. Configurar a Injeção de Dependência.
 * 2. Gerenciar o loop do menu principal.
 * 3. Delegar as ações para o Controller apropriado.
 */
class Master {

    // Helper View para o menu (poderia ser uma classe MainMenuView dedicada)
    // Usamos CandidatoView aqui apenas para ter acesso ao pedirIntAoUsuario
    private static final CandidatoView menuView = new CandidatoView()

    static void main(String[] args) {
        println "Iniciando aplicação LinkeTinder..."

        // --- 1. Configuração da Injeção de Dependência ---
        // (Exatamente como no 'main' anterior do Controller)
        IRepository repository = new RepositoryJdbc()
        PerfilService perfilService = new PerfilService(repository)
        CandidatoView candidatoView = new CandidatoView() // Instancia as Views concretas
        EmpresaView empresaView = new EmpresaView()
        PerfilController perfilController = new PerfilController(perfilService, candidatoView, empresaView)

        // --- 2. Loop do Menu Principal ---
        int opcao = -1
        while (opcao != 0) {
            mostrarMenuPrincipal() // Mostra as opções
            opcao = menuView.pedirIntAoUsuario("Digite sua opção:") // Pede a entrada

            // --- 3. Delegação para o Controller ---
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
                    // Adicionar cases para as outras opções aqui...
                case 0:
                    println "Encerrando aplicação..."
                    break
                default:
                    println "Opção inválida!"
            }

            // Pausa antes de mostrar o menu novamente (exceto ao sair)
            if (opcao != 0) {
                println "\nPressione Enter para continuar..."
                menuView.pedirStringAoUsuario("")
                println "\n----------------------------------\n"
            }
        }
    }

    /**
     * Método simples para exibir o menu principal.
     */
    private static void mostrarMenuPrincipal() {
        println "------ Menu Principal (Master) -------"
        println "1. Criar Candidato"
        println "2. Atualizar Candidato"
        println "3. Listar Candidatos"
        println "4. Criar Empresa"
        println "5. Atualizar Empresa"
        println "6. Listar Empresas"
        // ... (mais opções)
        println "0. Sair"
    }
}