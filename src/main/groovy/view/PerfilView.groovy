package view

import model.Candidato
import model.Empresa

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
        String descricao = pedirStringAoUsuario("Informe a descrição da empresa")
        String cep = pedirStringAoUsuario("Informe o cep da empresa:")
        String pais = pedirStringAoUsuario("Informe o país da empresa:")
        String senha = pedirStringAoUsuario("Digite uma senha para o usuário da empresa")

        println "Deseja criar alguma vaga?"
        List<Map<String, Object>> vagasParaCriar = new ArrayList<Map<String, Object>>()
        while (true){
            String resposta = pedirStringAoUsuario("Digite 'sim' para criar (ou 'nao' para pular)") // Melhoria
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

    Map<String, Object> pedirNovosDadosEmpresaParaAtualizar(Empresa empresa){
        println "\n --- Editando Empresa Nome: {$empresa.nome} (Id: {$empresa.id}) ---"
        println "Aperte 'Enter' para manter valor atual"

        String nome = pedirStringAoUsuarioComValorPadrao("Nome", empresa.nome)
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

    static void main(String[] args) {

        def pv = new PerfilView()

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        LocalDate data = LocalDate.parse("23/12/2005".trim(), formatador)
        Candidato c = new Candidato(idCandidato++, "Gabriel", "holanda@gfg", "feio", "32534563", "brasil", "123", ["java", "go"], "r3r355", data, "holanda")
        Candidato c1 = new Candidato(idCandidato++, "Maria", "hgsgsrda@gfg", "bonito", "12345678", "brasil", "456", ["python", "sql"], "r9r9r9", data, "Souza")
        ArrayList<Candidato> listaDeCandidatos = new ArrayList<Candidato>()
        listaDeCandidatos.add(c)
        listaDeCandidatos.add(c1)


        println "--- Sistema 'LinkeTinder' (Modo Teste da View) ---"


        while(true) {
            int opcao = pv.escolherOpcao()

            switch(opcao) {
                case 1:
                    Map<String, Object> dados = pv.pedirDadosAoUsuarioParaCriarCandidato()
                    println "\n[Main] Dados Coletados para Criar Candidato:"
                    println dados

                    break

                case 2:

                    Integer id = pv.pedirIdDoCandidatoParaAtualizar(listaDeCandidatos)
                    if (id == null) break // Lista vazia

                    Candidato candidatoParaAtualizar = listaDeCandidatos.find { it.id == id }

                    if (candidatoParaAtualizar) {

                        Map<String, Object> novosDados = pv.pedirNovosDadosCandidato(candidatoParaAtualizar)

                        println "\n[Main] Dados Coletados para ATUALIZAR Candidato (ID: $id):"
                        println novosDados


                        candidatoParaAtualizar.nome = novosDados.get("nome")
                        candidatoParaAtualizar.sobrenome = novosDados.get("sobrenome")
                        candidatoParaAtualizar.competencias = novosDados.get("competencias")

                    } else {
                        println "\n[Main] Erro: Candidato com ID $id não encontrado."
                    }
                    break

                case 3:
                    pv.mostrarListaDecandidatos(listaDeCandidatos)
                    break

                case 4:
                    Map<String, Object> dadosEmpresa = pv.pedirDadosAoUsuarioParaCriarEmpresa()
                    println "\n[Main] Dados Coletados para Criar Empresa:"
                    println dadosEmpresa
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