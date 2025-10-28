package view

import model.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class PerfilView {

    static Integer idCandidato = 1
    static Integer idEmpresa = 1
    static Integer idVaga = 1

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

        while (true) {
            String entrada = scanner.nextLine()
            try {
                int opcao = Integer.parseInt(entrada.trim())
                return opcao
            } catch (NumberFormatException e) {
                println "Opção Inválida"
                e.printStackTrace()
            }
        }
    }

    Map<String, Object> pedirDadosAoUsuarioParaCriarCandidato() {
        println "----- Cadastro de Novo Usuário -----"

        String nome = pedirStringAoUsuario("Informe seu Nome:")
        String sobrenome = pedirStringAoUsuario("Informe seu sobrenome")
        String cpf = pedirStringAoUsuario("Informe seu cpf")
        String email = pedirStringAoUsuario("Informe seu email:")
        String descricao = pedirStringAoUsuario("Informe sua descrição")
        String cep = pedirStringAoUsuario("Informe seu Cep:")
        String pais = pedirStringAoUsuario("Informe seu País:")
        LocalDate dataDeNascimento = pedirDataAoUsuario("Informe sua data de nascimento")
        String senha = pedirStringAoUsuario("Digite uma senha")

        List<String> competencias = new ArrayList<String>()
        println "Digite suas competẽncias(Java, Groovy...). Para finalizar digite 'fim' "
        while (true) {
            String competencia = pedirStringAoUsuario("Informe sua competência (ou 'fim'):")
            if (competencia.equalsIgnoreCase("fim")) {
                break
            }
            if (competencia && !competencia.trim().isEmpty()) {
                competencias << competencia.trim()
            }
        }
        Map<String, Object> dados = [
                "nome"            : nome,
                "sobrenome"       : sobrenome,
                "dataDeNascimento": dataDeNascimento,
                "cpf"             : cpf,
                "email"           : email,
                "descricao"       : descricao,
                "cep"             : cep,
                "pais"            : pais,
                "senha"           : senha,
                "competencias"    : competencias
        ]
        return dados
    }

    Map<String, Object> pedirDadosAoUsuarioParaCriarVaga() {
        println "----- Cadastro de Nova Vaga -----"
        String nome = pedirStringAoUsuario("Informe o nome da vaga:")
        String descricao = pedirStringAoUsuario("Descreva a vaga:")
        String local = pedirStringAoUsuario("Informe o local da vaga:")
        List<String> competencias = new ArrayList<String>()
        println "Digite as competências para a vaga(Java, Groovy...). Para finalizar digite 'fim' "
        while (true) {
            String competencia = pedirStringAoUsuario("Informe a competência (ou 'fim'):")
            if (competencia.equalsIgnoreCase("fim")) {
                break
            }
            if (competencia && !competencia.trim().isEmpty()) {
                competencias << competencia.trim()
            }
        }
        Map<String, Object> dados = [
                "nome"        : nome,
                "descricao"   : descricao,
                "local"       : local,
                "competencias": competencias
        ]
        return dados
    }

    Map<String, Object> pedirDadosAoUsuarioParaCriarEmpresa() {
        println "----- Cadastro de Nova Empresa -----"

        String nome = pedirStringAoUsuario("Informe o nome da empresa:")
        String cnpj = pedirStringAoUsuario("Informe o cnpj da empresa")
        String email = pedirStringAoUsuario("Informe o email da empresa:")
        String descricao = pedirStringAoUsuario("Informe a descrição da empresa:")
        String cep = pedirStringAoUsuario("Informe o cep da empresa:")
        String pais = pedirStringAoUsuario("Informe o país da empresa:")
        String senha = pedirStringAoUsuario("Digite uma senha para o usuário da empresa")

        println "Deseja criar alguma vaga?"
        List<Map<String, Object>> vagasParaCriar = new ArrayList<Map<String, Object>>()
        while (true) {
            String resposta = pedirStringAoUsuario("Deseja adicionar uma vaga? (Digite 'sim' ou 'fim' para parar)")
            if (resposta.equalsIgnoreCase("sim")) {
                Map<String, Object> dadosDaVaga = pedirDadosAoUsuarioParaCriarVaga()
                vagasParaCriar.add(dadosDaVaga)
                println "Vaga '${dadosDaVaga.get("nome")}' adicionada. Deseja adicionar outra?"
            } else {
                println "Cadastro de vagas finalizado."
                break
            }
        }
        Map<String, Object> dados = [
                "nome"     : nome,
                "cnpj"     : cnpj,
                "email"    : email,
                "descricao": descricao,
                "cep"      : cep,
                "pais"     : pais,
                "senha"    : senha,
                "vagas"    : vagasParaCriar
        ]
        return dados
    }

    Integer pedirIdDoCandidato(List<Candidato> candidatos) {
        println "\n---  Lista de Candidato com ID ---"

        if (candidatos.isEmpty()) {
            println "Nenhum candidato cadastrado."
            return null
        }

        println "Candidatos disponíveis:"
        candidatos.each { c ->
            println "  [ID: ${c.id}] - ${c.nome} ${c.sobrenome}"
        }

        return pedirIntAoUsuario("\nDigite o ID do candidato que deseja atualizar:")
    }

    Integer pedirIdDaEmpresa(List<Empresa> empresas) {
        println "\n---  Lista de Empresas com ID---"

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada."
            return null
        }

        println "Empresas disponíveis:"
        empresas.each { e ->
            println "  [ID: ${e.id}] - ${e.nome} (CNPJ: ${e.cnpj})"
        }

        return pedirIntAoUsuario("\nDigite o ID da empresa que deseja atualizar:")
    }

    Map<String, Object> pedirNovosDadosCandidatoParaAtualizar(Candidato candidatoAtual) {
        println "\n--- Editando Candidato: ${candidatoAtual.nome} (ID: ${candidatoAtual.id}) ---"
        println "(Pressione Enter para manter o valor atual para cada campo)"

        String nome = pedirStringAoUsuarioComValorPadrao("Nome", candidatoAtual.nome)
        String sobrenome = pedirStringAoUsuarioComValorPadrao("Sobrenome", candidatoAtual.sobrenome)
        String cpf = pedirStringAoUsuarioComValorPadrao("CPF", candidatoAtual.cpf)
        String email = pedirStringAoUsuarioComValorPadrao("Email", candidatoAtual.email)
        String descricao = pedirStringAoUsuarioComValorPadrao("Descrição", candidatoAtual.descricao)
        String cep = pedirStringAoUsuarioComValorPadrao("CEP", candidatoAtual.cep)
        String pais = pedirStringAoUsuarioComValorPadrao("País", candidatoAtual.pais)
        String senha = pedirStringAoUsuarioComValorPadrao("Senha", candidatoAtual.senha)
        LocalDate dataDeNascimento = pedirDataAoUsuarioComValorPadrao("Data de Nascimento", candidatoAtual.data_nascimento)

        List<String> competenciasFinais = candidatoAtual.competencias

        println "\n--- Gestão de Competências ---"
        println "Competências atuais: ${candidatoAtual.competencias.join(', ')}"
        String resposta = pedirStringAoUsuario("Deseja SUBSTITUIR esta lista por uma nova? (Digite 'sim' para refazer, ou Enter para manter a atual)")

        if (resposta.equalsIgnoreCase("sim")) {
            competenciasFinais = new ArrayList<>()
            println "OK. Digite as NOVAS competências, uma por uma."
            println "(Digite 'fim' quando terminar)"
            while (true) {
                String competencia = pedirStringAoUsuario("Nova competência (ou 'fim'):")
                if (competencia.equalsIgnoreCase("fim")) {
                    break
                }
                if (competencia && !competencia.trim().isEmpty()) {
                    competenciasFinais << competencia.trim()
                }
            }
            println "Nova lista de competências definida: $competenciasFinais"
        } else {
            println "Competências mantidas como estavam."
        }

        Map<String, Object> dados = [
                "nome"            : nome,
                "sobrenome"       : sobrenome,
                "dataDeNascimento": dataDeNascimento,
                "cpf"             : cpf,
                "email"           : email,
                "descricao"       : descricao,
                "cep"             : cep,
                "pais"            : pais,
                "senha"           : senha,
                "competencias"    : competenciasFinais
        ]
        return dados
    }

    Map<String, Object> pedirNovosDadosEmpresaParaAtualizar(Empresa empresa) {

        println "\n --- Editando Empresa Nome: ${empresa.nome} (Id: ${empresa.id}) ---"
        println "Aperte 'Enter' para manter valor atual"

        String nome = pedirStringAoUsuarioComValorPadrao("Nome", empresa.nome)
        String email = pedirStringAoUsuarioComValorPadrao("Email", empresa.email)
        String descricao = pedirStringAoUsuarioComValorPadrao("Descrição", empresa.descricao)
        String cep = pedirStringAoUsuarioComValorPadrao("Cep", empresa.cep)
        String pais = pedirStringAoUsuarioComValorPadrao("País", empresa.pais)
        String senha = pedirStringAoUsuarioComValorPadrao("Senha", empresa.senha)
        String cnpj = pedirStringAoUsuarioComValorPadrao("Cnpj", empresa.cnpj)


        println "\n--- Gestão de Vagas ---"

        if (empresa.vagas.isEmpty()) {
            println "Vagas atuais: Nenhuma"
        } else {
            println "Vagas atuais:"
            empresa.vagas.each { vaga ->
                println "  - [ID: ${vaga.id}] ${vaga.nome} (${vaga.competencias.size()} competências)"
            }
        }


        String resposta = pedirStringAoUsuario("\nDeseja SUBSTITUIR esta lista de vagas por uma nova? (Digite 'sim' para refazer, ou Enter para manter a atual)")

        List<Map<String, Object>> novasVagas = null

        if (resposta.equalsIgnoreCase("sim")) {
            novasVagas = new ArrayList<Map<String, Object>>()

            println "OK. Você irá criar uma nova lista de vagas do zero."
            println "(Digite 'fim' quando terminar de adicionar vagas)"

            while (true) {
                String respVaga = pedirStringAoUsuario("Deseja adicionar uma vaga? ('sim' para adicionar, 'fim' para parar)")

                if (respVaga.equalsIgnoreCase("fim")) {
                    break
                }

                if (respVaga.equalsIgnoreCase("sim")) {
                    Map<String, Object> dadosNovaVaga = pedirDadosAoUsuarioParaCriarVaga()
                    novasVagas.add(dadosNovaVaga)
                    println "Vaga '${dadosNovaVaga.get("nome")}' pronta para ser adicionada."
                }
            }
            println "Nova lista de vagas definida."


        } else {
            println "Lista de vagas mantida como estava."
        }

        Map<String, Object> dados = [
                "nome"     : nome,
                "cnpj"     : cnpj,
                "email"    : email,
                "descricao": descricao,
                "cep"      : cep,
                "pais"     : pais,
                "senha"    : senha,
                "vagas"    : novasVagas
        ]
        return dados
    }

    void mostrarListaDecandidatos(List<Candidato> candidatos) {
        println "\n----- Lista de Candidatos -----"

        if (candidatos.isEmpty()) {
            println "Lista de Candidatos está vazia"
        } else {
            candidatos.each { candidato ->

                println "[ID: ${candidato.id}] ${candidato.nome} ${candidato.sobrenome} (Competências: ${candidato.competencias.join(', ')})"
            }
        }
    }

    void mostrarListaDeEmpresas(List<Empresa> empresas) {
        println "\n----- Lista de Empresas -----"

        if (empresas.isEmpty()) {
            println "Lista de Empresas vazio"
        } else {
            empresas.each { empresa ->
                println "[ID: ${empresa.id}] ${empresa.nome} (CNPJ: ${empresa.cnpj})"

            }
        }
    }

    List<String> pedirCompetenciasParaAdicionar(Candidato candidato) {
        println "\n--- Adicionar Competências para ${candidato.nome} ---"
        println "Competências atuais: ${candidato.competencias.join(', ')}"

        List<String> novasCompetencias = []
        println "Digite as NOVAS competências. (Para finalizar digite 'fim')"

        while (true) {
            String competencia = pedirStringAoUsuario("Nova competência (ou 'fim'):")
            if (competencia.equalsIgnoreCase("fim")) {
                break
            }
            if (competencia && !competencia.trim().isEmpty() && !candidato.competencias.contains(competencia) && !novasCompetencias.contains(competencia)) {
                novasCompetencias << competencia.trim()
            } else {
                println "Competência inválida ou já existente."
            }
        }
        return novasCompetencias
    }

    String pedirCompetenciaParaRemover(Candidato candidato) {
        println "\n--- Remover Competência de ${candidato.nome} ---"
        if (candidato.competencias.isEmpty()) {
            println "Este candidato não possui competências para remover."
            return null
        }
        println "Competências atuais:"
        println "Competências atuais: ${candidato.competencias.join(', ')}"

        String competenciaRemover = pedirStringAoUsuario("\nDigite o NOME EXATO da competência a remover (ou 'cancelar'):")

        if (competenciaRemover.equalsIgnoreCase('cancelar') || !candidato.competencias.contains(competenciaRemover)) {
            println "Remoção cancelada ou competência não encontrada."
            return null
        }
        return competenciaRemover
    }

    Integer pedirVagaParaRemover(Empresa empresa) {
        println "\n--- Remover Vaga de ${empresa.nome} ---"
        if (empresa.vagas.isEmpty()) {
            println "Esta empresa não possui vagas para remover."
            return null
        }

        println "Vagas atuais desta empresa:"
        empresa.vagas.each { vaga ->
            println "  [ID: ${vaga.id}] ${vaga.nome}"
        }
        return pedirIntAoUsuario("\nDigite o ID da vaga que deseja DELETAR:")
    }

    // Métodos Auxiliares

    String pedirStringAoUsuario(String mensagemDoPedido) {
        println "$mensagemDoPedido"
        String string = scanner.nextLine()
        return string
    }

    int pedirIntAoUsuario(String mensagemDoPedido) {
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

    LocalDate pedirDataAoUsuario(String mensagemDoPedido) {
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

    String pedirStringAoUsuarioComValorPadrao(String mensagem, String valorPadrao) {
        println "$mensagem - valor atual: $valorPadrao"
        println "Digite o novo valor ou se desejar manter o valor aperte 'enter'"

        String novoValor = scanner.nextLine()
        if (novoValor.trim().isEmpty()) {
            return valorPadrao
        } else {
            return novoValor.trim()
        }
    }

    int pedirIntAoUsuarioComValorPadrao(String mensagem, int valorPadrao) {
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

    LocalDate pedirDataAoUsuarioComValorPadrao(String mensagem, LocalDate valorPadrao) {
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
                println "Data Inválida! Digite no formato dd/mm/aaaa ou pressione Enter."
                e.printStackTrace()
            }
        }
    }

    static void main(String[] args) {
        def candidatos = []
        def empresas = []
        def vagas = []

        println " --- Criação de Candidato"
        def pv = new PerfilView()
        println "Entrando no método da view"
        Map<String, Object> dadosParaCriarObjetoCandidato = pv.pedirDadosAoUsuarioParaCriarCandidato()
        println(dadosParaCriarObjetoCandidato)

        println "Passando Map para List de Candidatos"
        Candidato candidato = new Candidato(dadosParaCriarObjetoCandidato)
        candidato.setId(idCandidato)
        println(candidato)

        println " --- Criação de Vagas"
        println "Entrando no método da view"
        Map<String, Object> dadosParaCriarObjetoVaga = pv.pedirDadosAoUsuarioParaCriarVaga()
        println(dadosParaCriarObjetoVaga)

        try{
            println "Passando Map para Lista de vagas"
            Vaga vaga = new Vaga(dadosParaCriarObjetoVaga)
            vaga.setId(idVaga)
            println(vaga)
        } catch (Exception e){
            e.printStackTrace()
        }


        println " --- Criação de Empresa"
        println "Entrando no método da view"
        Map<String, Object> dadosParaCriarObjetoEmpresa = pv.pedirDadosAoUsuarioParaCriarEmpresa()
        println(dadosParaCriarObjetoEmpresa)
        try{
            println "Passando Map para Lista de empresa"
            Empresa empresa = new Empresa(dadosParaCriarObjetoEmpresa)
            empresa.setId(idEmpresa)
            println(empresa)
        }catch (Exception e){
            e.printStackTrace()
        }
    }
}