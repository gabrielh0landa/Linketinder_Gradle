package view

import model.Empresa

class EmpresaView extends BaseView {

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

        return pedirIntAoUsuario("\nDigite o ID da empresa que deseja selecionar:") // Mudei o texto
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
}