package controller

import model.Vaga
import repository.IRepository
import service.PerfilService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Scanner

class PerfilController {

    private final PerfilService servico = new PerfilService()
    private final IRepository repo
    private final SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd")

    PerfilController(IRepository repo) {
        this.repo = repo
        formatador.setLenient(false)
    }

    void exibirMenu(Scanner sc) {
        int opcao
        do {
            println "\n--- MENU ---"
            println "1. Criar Perfil de Candidato"
            println "2. Atualizar Perfil de Candidato"
            println "3. Listar Candidatos"
            println "4. Criar Perfil de Empresa"
            println "5. Atualizar Perfil de Empresa"
            println "6. Listar Empresas"
            println "7. Criar Vaga"
            println "8. Listar todas as Vagas"
            println "9. Listar Vagas por Empresa"
            println "0. Sair"
            print "Escolha uma opção: "

            try {
                opcao = sc.nextInt()
            } catch (Exception e) {
                println "Opção inválida."
                opcao = -1
            } finally {
                sc.nextLine()
            }

            switch (opcao) {
                case 0:
                    println "Saindo..."; break
                case 1:
                    print "Nome: "; def nome = sc.nextLine()
                    print "Sobrenome: "; def sobrenome = sc.nextLine()
                    print "CPF: "; def cpf = sc.nextLine()
                    Date dataNascimento = null
                    while (dataNascimento == null) {
                        print "Data de Nascimento (yyyy-MM-dd): "; def dataStr = sc.nextLine()
                        try { dataNascimento = formatador.parse(dataStr) } catch (Exception e) { println "Formato inválido." }
                    }
                    print "Email: "; def email = sc.nextLine()
                    print "Senha: "; def senha = sc.nextLine()
                    print "Descrição: "; def descricao = sc.nextLine()
                    print "CEP: "; def cep = sc.nextLine()
                    print "País: "; def pais = sc.nextLine()
                    def competencias = []; println "Competências (digite 'fim' para terminar):"
                    while (true) { def entrada = sc.nextLine(); if (entrada.equalsIgnoreCase("fim")) break; if(entrada) competencias << entrada }
                    def c = servico.criarNovoCandidato(nome, sobrenome, email, descricao, cep, pais, senha, competencias, cpf, dataNascimento)
                    repo.adicionarCandidato(c)
                    println "\nCandidato adicionado com sucesso!"
                    break
                case 2:
                    def listaC = repo.listarCandidatos()
                    if (listaC.empty) { println "Não há candidatos."; break }
                    listaC.eachWithIndex { cand, i -> println "$i: ${cand.nome} ${cand.sobrenome}" }
                    print "Qual índice deseja atualizar? "; int indice = sc.nextInt(); sc.nextLine()
                    if (indice < 0 || indice >= listaC.size()) { println "Índice inválido."; break }
                    def cAtual = listaC[indice]
                    println "Pressione Enter para manter o valor atual."
                    print "Nome (${cAtual.nome}): "; def novoNome = sc.nextLine() ?: null
                    print "Sobrenome (${cAtual.sobrenome}): "; def novoSobrenome = sc.nextLine() ?: null
                    repo.atualizarCandidato(cAtual.id, cAtual)
                    println "\nCandidato atualizado com sucesso!"
                    break
                case 3:
                    println "\n--- Lista de Candidatos ---"
                    repo.listarCandidatos().each { println it }
                    break
                case 4:
                    print "Nome: "; def nomeE = sc.nextLine()
                    print "CNPJ: "; def cnpj = sc.nextLine()
                    print "Email: "; def emailE = sc.nextLine()
                    print "Senha: "; def senhaE = sc.nextLine()
                    print "Descrição: "; def descricaoE = sc.nextLine()
                    print "CEP: "; def cepE = sc.nextLine()
                    print "País: "; def paisE = sc.nextLine()
                    def competenciasE = []; println "Áreas (digite 'fim' para terminar):"
                    while (true) { def entrada = sc.nextLine(); if (entrada.equalsIgnoreCase("fim")) break; if(entrada) competenciasE << entrada }
                    def e = servico.criarNovaEmpresa(nomeE, cnpj, emailE, descricaoE, cepE, paisE, senhaE, competenciasE)
                    repo.adicionarEmpresa(e)
                    println "\nEmpresa criada com sucesso!"
                    break
                case 5:
                    def listaE = repo.listarEmpresas()
                    if (listaE.empty) { println "Não há empresas."; break }
                    listaE.eachWithIndex { emp, i -> println "$i: ${emp.nome}" }
                    print "Qual índice deseja atualizar? "; int indiceE = sc.nextInt(); sc.nextLine()
                    if (indiceE < 0 || indiceE >= listaE.size()) { println "Índice inválido."; break }
                    def eAtual = listaE[indiceE]
                    println "Pressione Enter para manter o valor atual."
                    print "Nome (${eAtual.nome}): "; def novoNomeE = sc.nextLine() ?: null
                    print "CNPJ (${eAtual.cnpj}): "; def novoCnpj = sc.nextLine() ?: null
                    repo.atualizarEmpresa(eAtual.id, eAtual)
                    println "\nEmpresa atualizada com sucesso!"
                    break
                case 6:
                    println "\n--- Lista de Empresas ---"
                    repo.listarEmpresas().each { println it }
                    break
                case 7:
                    def empresas = repo.listarEmpresas()
                    if (empresas.empty) { println "É preciso ter uma empresa cadastrada para criar uma vaga."; break }
                    empresas.eachWithIndex { emp, i -> println "$i: ${emp.nome}" }
                    print "Para qual empresa é a vaga? (índice): "; int indiceEmp = sc.nextInt(); sc.nextLine()
                    if (indiceEmp < 0 || indiceEmp >= empresas.size()) { println "Índice inválido."; break }
                    def empresaSelecionada = empresas[indiceEmp]
                    print "Nome da vaga: "; def nomeVaga = sc.nextLine()
                    print "Descrição: "; def descVaga = sc.nextLine()
                    print "Local (Ex: Remoto, São Paulo - SP): "; def localVaga = sc.nextLine()
                    def compVaga = []; println "Competências necessárias (digite 'fim' para terminar):"
                    while (true) { def entrada = sc.nextLine(); if (entrada.equalsIgnoreCase("fim")) break; if(entrada) compVaga << entrada }
                    def vaga = servico.criarNovaVaga(nomeVaga, descVaga, localVaga, empresaSelecionada.id, compVaga)
                    repo.adicionarVaga(vaga)
                    println "\nVaga criada com sucesso!"
                    break
                case 8:
                    println "\n--- Todas as Vagas ---"
                    repo.listarTodasVagas().each { println it }
                    break
                case 9:
                    def listaEmp = repo.listarEmpresas()
                    if (listaEmp.empty) { println "Nenhuma empresa cadastrada."; break }
                    listaEmp.eachWithIndex { emp, i -> println "$i: ${emp.nome}" }
                    print "Listar vagas de qual empresa? (índice): "; int idxEmp = sc.nextInt(); sc.nextLine()
                    if (idxEmp < 0 || idxEmp >= listaEmp.size()) { println "Índice inválido."; break }
                    def empSel = listaEmp[idxEmp]
                    def vagas = repo.listarVagasPorEmpresa(empSel.id)
                    if (vagas.empty) {
                        println "Nenhuma vaga encontrada para ${empSel.nome}."
                    } else {
                        println "\n--- Vagas para ${empSel.nome} ---"
                        vagas.each { println it }
                    }
                    break
                default:
                    println "Opção inválida!"
            }
        } while (opcao != 0)
    }
}