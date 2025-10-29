package controller

import model.Candidato
import model.Empresa
import model.Vaga
import service.PerfilService
import view.CandidatoView
import view.EmpresaView
import java.util.NoSuchElementException

class PerfilController {

    private final CandidatoView candidatoView
    private final EmpresaView empresaView
    private final PerfilService service

    PerfilController(PerfilService service, CandidatoView candidatoView, EmpresaView empresaView) {
        this.service = service
        this.candidatoView = candidatoView
        this.empresaView = empresaView
    }

    void criarCandidato() {
        println "\n--- 1. Criar Novo Candidato ---"
        try {
            Map dados = candidatoView.pedirDadosAoUsuarioParaCriarCandidato()
            Candidato novoCandidato = service.criarCandidato(dados)
            println "\nCandidato criado com sucesso!"
            novoCandidato.exibirPerfil()
        } catch (IllegalArgumentException e) {
            println "\nERRO DE VALIDAÇÃO: ${e.message}"
        } catch (Exception e) {
            println "\nERRO INESPERADO AO CRIAR CANDIDATO: ${e.message}"
            e.printStackTrace()
        }
    }

    void listarCandidatos() {
        println "\n--- 3. Listar Candidatos ---"
        try {
            List<Candidato> candidatos = service.listarCandidatos()
            candidatoView.mostrarListaDecandidatos(candidatos)
        } catch (Exception e) {
            println "\nERRO INESPERADO AO LISTAR CANDIDATOS: ${e.message}"
            e.printStackTrace()
        }
    }

    void atualizarCandidato() {
        println "\n--- 2. Atualizar Candidato ---"
        try {
            List<Candidato> listaAtual = service.listarCandidatos()
            Integer id = candidatoView.pedirIdDoCandidato(listaAtual)
            if (id == null) {
                println "Operação cancelada."
                return
            }

            Candidato candidatoOriginal = service.buscarCandidato(id)
            if (candidatoOriginal == null) {
                println "Candidato não encontrado."
                return
            }

            Map novosDados = candidatoView.pedirNovosDadosCandidatoParaAtualizar(candidatoOriginal)
            Candidato candidatoAtualizado = service.atualizarCandidato(id, novosDados)

            println "\nCandidato atualizado com sucesso!"
            candidatoAtualizado.exibirPerfil()

        } catch (IllegalArgumentException e) {
            println "\nERRO DE VALIDAÇÃO: ${e.message}"
        } catch (Exception e) {
            println "\nERRO INESPERADO AO ATUALIZAR CANDIDATO: ${e.message}"
            e.printStackTrace()
        }
    }

    void criarEmpresa() {
        println "\n--- 4. Criar Nova Empresa ---"
        try {
            Map dados = empresaView.pedirDadosAoUsuarioParaCriarEmpresa()
            Empresa novaEmpresa = service.criarEmpresa(dados)
            println "\nEmpresa criada com sucesso!"
            novaEmpresa.exibirPerfil()
        } catch (IllegalArgumentException e) {
            println "\nERRO DE VALIDAÇÃO: ${e.message}"
        } catch (Exception e) {
            println "\nERRO INESPERADO AO CRIAR EMPRESA: ${e.message}"
            e.printStackTrace()
        }
    }

    void listarEmpresas() {
        println "\n--- 6. Listar Empresas ---"
        try {
            List<Empresa> empresas = service.listarEmpresas()
            empresaView.mostrarListaDeEmpresas(empresas)
        } catch (Exception e) {
            println "\nERRO INESPERADO AO LISTAR EMPRESAS: ${e.message}"
            e.printStackTrace()
        }
    }

    void atualizarEmpresa() {
        println "\n--- 5. Atualizar Empresa ---"
        try {
            List<Empresa> listaAtual = service.listarEmpresas()
            Integer id = empresaView.pedirIdDaEmpresa(listaAtual)
            if (id == null) {
                println "Operação cancelada."
                return
            }

            Empresa empresaOriginal = service.buscarEmpresa(id)
            if (empresaOriginal == null) {
                println "Empresa não encontrada."
                return
            }

            Map novosDados = empresaView.pedirNovosDadosEmpresaParaAtualizar(empresaOriginal)
            Empresa empresaAtualizada = service.atualizarEmpresa(id, novosDados)

            println "\nEmpresa atualizada com sucesso!"
            empresaAtualizada.exibirPerfil()

        } catch (IllegalArgumentException | NoSuchElementException e) {
            println "\nERRO: ${e.message}"
        } catch (Exception e) {
            println "\nERRO INESPERADO AO ATUALIZAR EMPRESA: ${e.message}"
            e.printStackTrace()
        }
    }
}