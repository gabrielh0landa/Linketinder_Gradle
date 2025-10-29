package view

import model.Candidato
import java.time.LocalDate

class CandidatoView extends BaseView {

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

        return pedirIntAoUsuario("\nDigite o ID do candidato que deseja selecionar:") // Mudei o texto
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
        LocalDate dataDeNascimento = pedirDataAoUsuarioComValorPadrao("Data de Nascimento", candidatoAtual.dataDeNascimento)

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
        println "Competências atuais: ${candidato.competencias.join(', ')}"

        String competenciaRemover = pedirStringAoUsuario("\nDigite o NOME EXATO da competência a remover (ou 'cancelar'):")

        if (competenciaRemover.equalsIgnoreCase('cancelar') || !candidato.competencias.contains(competenciaRemover)) {
            println "Remoção cancelada ou competência não encontrada."
            return null
        }
        return competenciaRemover
    }
}