package view

import model.Candidato
import model.Empresa
import model.Vaga

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Scanner
import java.util.ArrayList

class PerfilView {

    static idCandidato = 1
    static idEmpresa = 1
    static idVaga = 1

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
        while (true){
            String competencia = pedirStringAoUsuario("Informe sua competência (ou 'fim'):")
            if (competencia.equalsIgnoreCase("fim")){
                break
            }
            if (competencia && !competencia.trim().isEmpty()){
                competencias << competencia.trim()
            }
        }
        Map <String, Object>dados = [
                "nome": nome,
                "sobrenome": sobrenome,
                "dataDeNascimento": dataDeNascimento,
                "cpf": cpf,
                "email": email,
                "descricao": descricao,
                "cep": cep,
                "pais": pais,
                "senha": senha,
                "competencias": competencias
        ]
        return dados
    }

    Map<String, Object> pedirDadosAoUsuarioParaCriarVaga(){
        println "----- Cadastro de Nova Vaga -----"
        String nome = pedirStringAoUsuario("Informe o nome da vaga:")
        String descricao = pedirStringAoUsuario("Descreva a vaga:")
        String local = pedirStringAoUsuario("Informe o local da vaga:")
        List<String> competencias = new ArrayList<String>()
        println "Digite as competências para a vaga(Java, Groovy...). Para finalizar digite 'fim' "
        while (true){
            String competencia = pedirStringAoUsuario("Informe a competência (ou 'fim'):")
            if (competencia.equalsIgnoreCase("fim")){
                break
            }
            if (competencia && !competencia.trim().isEmpty()){
                competencias << competencia.trim()
            }
        }
        Map <String, Object>dados = [
                "nome": nome,
                "descricao": descricao,
                "local": local,
                "competencias": competencias
        ]
        return dados
    }

    Map<String, Object> pedirDadosAoUsuarioParaCriarEmpresa(){
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
        while (true){
            String resposta = pedirStringAoUsuario("Digite 'sim' para criar (ou 'nao' para pular)")
            if (resposta.equalsIgnoreCase("sim")){
                Map<String, Object> dadosDaVaga =  pedirDadosAoUsuarioParaCriarVaga()
                vagasParaCriar.add(dadosDaVaga)
                println "Vaga '${dadosDaVaga.get("nome")}' adicionada. Deseja adicionar outra?"
            } else {
                println "Cadastro de vagas finalizado."
                break
            }
        }
        Map<String, Object> dados = [
                "nome": nome,
                "cnpj": cnpj,
                "email": email,
                "descricao": descricao,
                "cep": cep,
                "pais": pais,
                "senha": senha,
                "vagas": vagasParaCriar
        ]
        return dados
    }

    Integer pedirIdDoCandidatoParaAtualizar(List<Candidato> candidatos) {
        println "\n--- ⬆️ Atualizar Candidato ---"

        if (candidatos.isEmpty()) {
            println "Nenhum candidato cadastrado para atualizar."
            return null
        }

        println "Candidatos disponíveis:"
        candidatos.each { c ->
            println "  [ID: ${c.id}] - ${c.nome} ${c.sobrenome}"
        }

        return pedirIntAoUsuario("\nDigite o ID do candidato que deseja atualizar:")
    }

    Integer pedirIdDaEmpresaParaAtualizar(List<Empresa> empresas) {
        println "\n--- ⬆️ Atualizar Empresa ---"

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada para atualizar."
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

        Map <String, Object> dados = [
                "nome": nome,
                "sobrenome": sobrenome,
                "dataDeNascimento": dataDeNascimento,
                "cpf": cpf,
                "email": email,
                "descricao": descricao,
                "cep": cep,
                "pais": pais,
                "senha": senha,
                "competencias": competenciasFinais
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

        // --- INÍCIO DA CORREÇÃO (BLOCO 1) ---
        // Seu código estava faltando esta parte para mostrar as vagas
        if (empresa.vagas.isEmpty()) {
            println "Vagas atuais: Nenhuma"
        } else {
            println "Vagas atuais:"
            empresa.vagas.each { vaga ->
                println "  - [ID: ${vaga.id}] ${vaga.nome} (${vaga.competencias.size()} competências)"
            }
        }
        // --- FIM DA CORREÇÃO (BLOCO 1) ---

        String resposta = pedirStringAoUsuario("\nDeseja SUBSTITUIR esta lista de vagas por uma nova? (Digite 'sim' para refazer, ou Enter para manter a atual)")

        List<Map<String, Object>> novasVagas = null // 'null' significa "manter"

        // --- INÍCIO DA CORREÇÃO (BLOCO 2) ---
        // Seu bloco 'if' estava vazio.
        if (resposta.equalsIgnoreCase("sim")) {

            novasVagas = new ArrayList<Map<String, Object>>() // Cria a lista

            println "OK. Você irá criar uma nova lista de vagas do zero."
            println "(Digite 'fim' quando terminar de adicionar vagas)"

            while (true) {
                String respVaga = pedirStringAoUsuario("Deseja adicionar uma vaga? ('sim' para adicionar, 'fim' para parar)")

                if (respVaga.equalsIgnoreCase("fim")) {
                    break // Para o loop de adicionar vagas
                }

                if (respVaga.equalsIgnoreCase("sim")) {
                    // Re-usa o método que você já tem!
                    Map<String, Object> dadosNovaVaga = pedirDadosAoUsuarioParaCriarVaga()
                    novasVagas.add(dadosNovaVaga)
                    println "Vaga '${dadosNovaVaga.get("nome")}' pronta para ser adicionada."
                }
            }
            println "Nova lista de vagas definida."
            // --- FIM DA CORREÇÃO (BLOCO 2) ---

        } else {
            println "Lista de vagas mantida como estava."
        }

        Map<String, Object> dados = [
                "nome": nome,
                "cnpj": cnpj,
                "email": email,
                "descricao": descricao,
                "cep": cep,
                "pais": pais,
                "senha": senha,
                "vagas": novasVagas // 'novasVagas' será 'null' ou a nova lista de Mapas
        ]
        return dados
    }

    void mostrarListaDecandidatos(List<Candidato> candidatos){
        println "\n----- Lista de Candidatos -----"

        if (candidatos.isEmpty()){
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

    String pedirStringAoUsuarioComValorPadrao(String mensagem, String valorPadrao){
        println "$mensagem - valor atual: $valorPadrao"
        println "Digite o novo valor ou se desejar manter o valor aperte 'enter'"

        String novoValor = scanner.nextLine()
        if (novoValor.trim().isEmpty()){
            return valorPadrao
        } else {
            return novoValor.trim()
        }
    }

    int pedirIntAoUsuarioComValorPadrao(String mensagem, int valorPadrao){
        println "$mensagem - valor atual: $valorPadrao"
        println "Digite o novo valor ou se desejar manter o valor aperte 'enter'"

        while(true) {
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

        def pv = new PerfilView()

        // --- Simulação de Banco de Dados ---
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        LocalDate data = LocalDate.parse("23/12/2005".trim(), formatador)

        // Mock de Candidatos (como você já tinha)
        Candidato c = new Candidato(idCandidato++, "Gabriel", "holanda@gfg", "feio", "32534563", "brasil", "123", ["java", "go"], "r3r355", data, "holanda")
        Candidato c1 = new Candidato(idCandidato++, "Maria", "hgsgsrda@gfg", "bonito", "12345678", "brasil", "456", ["python", "sql"], "r9r9r9", data, "Souza")
        ArrayList<Candidato> listaDeCandidatos = new ArrayList<Candidato>()
        listaDeCandidatos.add(c)
        listaDeCandidatos.add(c1)

        // --- NOVO: Mock de Empresas e Vagas ---
        ArrayList<Empresa> listaDeEmpresas = new ArrayList<>()

        // Empresa 1
        Empresa e1 = new Empresa(idEmpresa, "Tech Solutions", "contato@tech.com", "Empresa de TI", "12345-000", "Brasil", "senha123", "11.111.111/0001-11", [])
        Vaga v1 = new Vaga(idVaga++, "Dev Java", "Vaga para Java Sr.", "Remoto", e1.id, ["Java", "Spring"])
        Vaga v2 = new Vaga(idVaga++, "Dev Groovy", "Vaga para Groovy", "Híbrido", e1.id, ["Groovy", "Grails"])
        e1.vagas.addAll([v1, v2])
        listaDeEmpresas.add(e1)
        idEmpresa++ // Incrementa o ID para a próxima empresa

        // Empresa 2
        Empresa e2 = new Empresa(idEmpresa, "Data Corp", "rh@datacorp.com", "Empresa de Dados", "54321-000", "Brasil", "senha456", "22.222.222/0001-22", [])
        Vaga v3 = new Vaga(idVaga++, "Engenheiro de Dados", "Vaga de DE", "Presencial", e2.id, ["Python", "Spark", "SQL"])
        e2.vagas.add(v3)
        listaDeEmpresas.add(e2)
        idEmpresa++ // Incrementa
        // --- Fim da Simulação ---


        println "--- Sistema 'LinkeTinder' (Modo Teste da View) ---"


        while(true) {
            int opcao = pv.escolherOpcao()

            switch(opcao) {
                case 1: // Criar Candidato
                    Map<String, Object> dados = pv.pedirDadosAoUsuarioParaCriarCandidato()

                    // Simulação do Controller...
                    Candidato novoCandidato = new Candidato(
                            idCandidato++, // Usa o ID estático e incrementa
                            dados.get("nome"), dados.get("email"), dados.get("descricao"),
                            dados.get("cep"), dados.get("pais"), dados.get("senha"),
                            dados.get("competencias"), dados.get("cpf"),
                            dados.get("dataDeNascimento"), dados.get("sobrenome")
                    )
                    listaDeCandidatos.add(novoCandidato)
                    println "\n[Main] Candidato '${novoCandidato.nome}' (ID: ${novoCandidato.id}) adicionado com sucesso!"
                    break

                case 2: // Atualizar Candidato
                    Integer id = pv.pedirIdDoCandidatoParaAtualizar(listaDeCandidatos)
                    if (id == null) break

                    Candidato candidatoParaAtualizar = listaDeCandidatos.find { it.id == id }

                    if (candidatoParaAtualizar) {
                        Map<String, Object> novosDados = pv.pedirNovosDadosCandidatoParaAtualizar(candidatoParaAtualizar)

                        println "\n[Main] Dados Coletados para ATUALIZAR Candidato (ID: $id):"
                        println novosDados

                        // Atualiza o objeto na lista
                        candidatoParaAtualizar.nome = novosDados.get("nome")
                        candidatoParaAtualizar.sobrenome = novosDados.get("sobrenome")
                        candidatoParaAtualizar.competencias = novosDados.get("competencias")
                        candidatoParaAtualizar.cpf = novosDados.get("cpf")
                        candidatoParaAtualizar.email = novosDados.get("email")
                        candidatoParaAtualizar.descricao = novosDados.get("descricao")
                        // ... etc
                    } else {
                        println "\n[Main] Erro: Candidato com ID $id não encontrado."
                    }
                    break

                case 3: // Listar Candidatos
                    pv.mostrarListaDecandidatos(listaDeCandidatos)
                    break

                case 4: // Criar Empresa
                    Map<String, Object> dadosEmpresa = pv.pedirDadosAoUsuarioParaCriarEmpresa()

                    // Simulação do Controller...
                    // 1. Cria a empresa (sem vagas)
                    Empresa novaEmpresa = new Empresa(
                            idEmpresa, // ID estático
                            dadosEmpresa.get("nome"), dadosEmpresa.get("email"), dadosEmpresa.get("descricao"),
                            dadosEmpresa.get("cep"), dadosEmpresa.get("pais"), dadosEmpresa.get("senha"),
                            [], // Competencias herdadas (ignoramos)
                            dadosEmpresa.get("cnpj"),
                            []  // Lista de vagas vazia
                    )

                    // 2. "Traduz" os Mapas de vagas em Objetos Vaga
                    List<Map<String, Object>> dadosVagas = dadosEmpresa.get("vagas")
                    dadosVagas.each { mapVaga ->
                        Vaga novaVaga = new Vaga(
                                idVaga++, // ID estático da vaga
                                mapVaga.get("nome"),
                                mapVaga.get("descricao"),
                                mapVaga.get("local"),
                                novaEmpresa.id, // Conecta a vaga à empresa
                                mapVaga.get("competencias")
                        )
                        novaEmpresa.vagas.add(novaVaga) // Adiciona a vaga na lista da empresa
                    }

                    // 3. Adiciona a empresa (com suas vagas) na lista principal
                    listaDeEmpresas.add(novaEmpresa)
                    idEmpresa++ // Incrementa o ID estático para a *próxima* empresa

                    println "\n[Main] Empresa '${novaEmpresa.nome}' (ID: ${novaEmpresa.id}) e ${novaEmpresa.vagas.size()} vagas adicionadas!"
                    break

                case 5: // Atualizar Empresa
                    Integer idEmp = pv.pedirIdDaEmpresaParaAtualizar(listaDeEmpresas)
                    if (idEmp == null) break // Lista vazia

                    Empresa empresaParaAtualizar = listaDeEmpresas.find { it.id == idEmp }

                    if (empresaParaAtualizar) {
                        Map<String, Object> novosDadosEmp = pv.pedirNovosDadosEmpresaParaAtualizar(empresaParaAtualizar)

                        println "\n[Main] Dados Coletados para ATUALIZAR Empresa (ID: $idEmp):"
                        println novosDadosEmp

                        // Atualiza campos simples
                        empresaParaAtualizar.nome = novosDadosEmp.get("nome")
                        empresaParaAtualizar.cnpj = novosDadosEmp.get("cnpj")
                        empresaParaAtualizar.email = novosDadosEmp.get("email")
                        // ... etc

                        // Lógica de Vagas (como o controller faria)
                        List<Map<String, Object>> dadosNovasVagas = novosDadosEmp.get("vagas")
                        if (dadosNovasVagas != null) { // 'null' significa "manter"

                            println "[Main] Atualizando lista de vagas..."
                            // 1. Remove as vagas antigas
                            empresaParaAtualizar.vagas.clear()

                            // 2. Adiciona as novas vagas (traduzindo do Map)
                            dadosNovasVagas.each { mapVaga ->
                                Vaga novaVaga = new Vaga(
                                        idVaga++, // Novo ID
                                        mapVaga.get("nome"),
                                        mapVaga.get("descricao"),
                                        mapVaga.get("local"),
                                        empresaParaAtualizar.id, // ID da empresa
                                        mapVaga.get("competencias")
                                )
                                empresaParaAtualizar.vagas.add(novaVaga)
                            }
                            println "[Main] Lista de vagas substituída."
                        } else {
                            println "[Main] Lista de vagas mantida."
                        }

                    } else {
                        println "\n[Main] Erro: Empresa com ID $idEmp não encontrada."
                    }
                    break

                case 6: // Listar Empresas
                    pv.mostrarListaDeEmpresas(listaDeEmpresas)
                    break

                case 0:
                    println "Saindo..."
                    return

                default:
                    println "[Main] Opção $opcao ainda não implementada."
            }
            println "\n----------------------------------\n"
        }
    }
}