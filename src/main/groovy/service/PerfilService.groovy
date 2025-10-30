package service

import model.Candidato
import model.Empresa
import model.Vaga
import repository.IRepository
import java.time.LocalDate

class PerfilService {

    private final IRepository repository

    PerfilService(IRepository repository) {
        this.repository = repository
    }

    Candidato criarCandidato(Map dados) {
        Candidato candidato = new Candidato(dados)
        validarCandidato(candidato)
        repository.adicionarCandidato(candidato)
        return candidato
    }

    private void validarCandidato(Candidato candidato) {
        if (!candidato.nome?.trim()) throw new IllegalArgumentException("Nome é obrigatório.")
        if (!candidato.sobrenome?.trim()) throw new IllegalArgumentException("Sobrenome é obrigatório.")
        if (!candidato.email?.trim()) throw new IllegalArgumentException("Email é obrigatório.")
        if (!candidato.cpf?.trim()) throw new IllegalArgumentException("CPF é obrigatório.")
        if (candidato.dataDeNascimento == null) throw new IllegalArgumentException("Data de nascimento é obrigatória.")
        if (!candidato.senha?.trim()) throw new IllegalArgumentException("Senha é obrigatória.")

        def emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
        if (!(candidato.email =~ emailRegex)) {
            throw new IllegalArgumentException("Formato de email inválido.")
        }

        def cpfRegex = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/
        if (!(candidato.cpf =~ cpfRegex)) {
            def cpfNumerico = candidato.cpf.replaceAll(/\D/, '')
            if (cpfNumerico.size() == 11) {
                candidato.cpf = cpfNumerico.replaceAll(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4')
                if (!(candidato.cpf =~ cpfRegex)) {
                    throw new IllegalArgumentException("Formato de CPF inválido (esperado XXX.XXX.XXX-XX).")
                }
            } else {
                throw new IllegalArgumentException("Formato de CPF inválido (esperado XXX.XXX.XXX-XX).")
            }
        }

        if (candidato.dataDeNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser no futuro.")
        }

        if (candidato.competencias == null || candidato.competencias.isEmpty()) {
        }
    }

    List<Candidato> listarCandidatos() {
        return repository.listarCandidatos()
    }

    Candidato buscarCandidato(int id) {
        Candidato candidato = repository.buscarCandidato(id)
        if (candidato == null) {
            return null
        }
        return candidato
    }

    Candidato atualizarCandidato(int id, Map novosDados) {
        Candidato candidatoAtual = buscarCandidato(id)
        if (candidatoAtual == null) {
            throw new IllegalArgumentException("Erro: Candidato com ID $id não encontrado para atualização.")
        }

        novosDados.each { key, value ->
            if (candidatoAtual.hasProperty(key) && key != 'class' && key != 'metaClass') {
                try {
                    candidatoAtual."$key" = value
                } catch (ReadOnlyPropertyException roe) {
                }
            }
        }

        validarCandidato(candidatoAtual)
        repository.atualizarCandidato(id, candidatoAtual)
        return candidatoAtual
    }

    Candidato adicionarCompetenciasParaCandidato(int candidatoId, List<String> novasCompetencias) {
        Candidato candidato = buscarCandidato(candidatoId) // Reusa o método buscar
        if (candidato == null) {
            throw new NoSuchElementException("Candidato com ID $candidatoId não encontrado.")
        }

        int adicionadas = 0
        novasCompetencias.each { novaComp ->
            if (novaComp?.trim() && !candidato.competencias.contains(novaComp.trim())) {
                candidato.competencias.add(novaComp.trim())
                adicionadas++
            }
        }

        if (adicionadas == 0) {
            println "[Service] Nenhuma competência nova foi adicionada (já existiam ou eram inválidas)."
            return candidato
        }

        repository.atualizarCandidato(candidatoId, candidato)
        println "[Service] $adicionadas competências adicionadas."
        return candidato
    }

    Candidato removerCompetenciaDeCandidato(int candidatoId, String competenciaParaRemover) {
        Candidato candidato = buscarCandidato(candidatoId)
        if (candidato == null) {
            throw new NoSuchElementException("Candidato com ID $candidatoId não encontrado.")
        }

        if (!competenciaParaRemover || !candidato.competencias.contains(competenciaParaRemover)) {
            throw new IllegalArgumentException("Competência '${competenciaParaRemover}' não encontrada neste candidato.")
        }
        candidato.competencias.remove(competenciaParaRemover)

        repository.atualizarCandidato(candidatoId, candidato)
        println "[Service] Competência removida."
        return candidato
    }

    void deletarCandidato(int candidatoId) {
        println "[Service] Tentando deletar candidato ID: $candidatoId"
        Candidato candidato = buscarCandidato(candidatoId)
        if (candidato == null) {
            throw new NoSuchElementException("Candidato com ID $candidatoId não encontrado para deletar.")
        }
        repository.deletarCandidato(candidatoId)
        println "[Service] Candidato ID $candidatoId deletado com sucesso."
    }


    void deletarEmpresa(int empresaId) {
        println "[Service] Tentando deletar empresa ID: $empresaId"
        Empresa empresa = buscarEmpresa(empresaId)
        if (empresa == null) {
            throw new NoSuchElementException("Empresa com ID $empresaId não encontrada para deletar.")
        }

        repository.deletarEmpresa(empresaId)
        println "[Service] Empresa ID $empresaId e todas as suas vagas associadas foram deletadas."
    }

    Empresa criarEmpresa(Map dadosEmpresa) {
        Empresa empresa = new Empresa(dadosEmpresa)
        validarEmpresa(empresa)

        repository.adicionarEmpresa(empresa)

        List<Map> dadosVagas = (List<Map>) dadosEmpresa.get("vagas") ?: []
        List<Vaga> vagasCriadas = []

        dadosVagas.each { mapVaga ->
            Vaga vaga = new Vaga(mapVaga)
            vaga.empresa = empresa
            validarVaga(vaga)
            repository.adicionarVaga(vaga)
            vagasCriadas.add(vaga)
        }

        empresa.vagas = vagasCriadas
        return empresa
    }

    private void validarEmpresa(Empresa empresa) {
        if (!empresa.nome?.trim()) throw new IllegalArgumentException("Nome da empresa é obrigatório.")
        if (!empresa.cnpj?.trim()) throw new IllegalArgumentException("CNPJ é obrigatório.")
        if (!empresa.email?.trim()) throw new IllegalArgumentException("Email da empresa é obrigatório.")
        if (!empresa.senha?.trim()) throw new IllegalArgumentException("Senha da empresa é obrigatória.")

        def cnpjRegex = /^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/
        if (!(empresa.cnpj =~ cnpjRegex)) {
            def cnpjNumerico = empresa.cnpj.replaceAll(/\D/, '')
            if (cnpjNumerico.size() == 14) {
                empresa.cnpj = cnpjNumerico.replaceAll(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5')
                if (!(empresa.cnpj =~ cnpjRegex)) {
                    throw new IllegalArgumentException("Formato de CNPJ inválido (esperado XX.XXX.XXX/XXXX-XX).")
                }
            } else {
                throw new IllegalArgumentException("Formato de CNPJ inválido (esperado XX.XXX.XXX/XXXX-XX).")
            }
        }

        def emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
        if (!(empresa.email =~ emailRegex)) {
            throw new IllegalArgumentException("Formato de email inválido para a empresa.")
        }
    }

    private void validarVaga(Vaga vaga) {
        if (!vaga.nome?.trim()) throw new IllegalArgumentException("Nome da vaga é obrigatório.")
        if (!vaga.descricao?.trim()) throw new IllegalArgumentException("Descrição da vaga é obrigatória.")
        if (vaga.empresa == null || vaga.empresa.id == null) {
            throw new IllegalStateException("Vaga deve estar associada a uma empresa salva.")
        }
        if (vaga.competencias == null || vaga.competencias.isEmpty()) {
        }
    }

    List<Empresa> listarEmpresas() {
        return repository.listarEmpresas()
    }

    Empresa buscarEmpresa(int id) {
        Empresa empresa = repository.buscarEmpresa(id)
        if (empresa == null) {
            return null
        }
        return empresa
    }

    Empresa atualizarEmpresa(int id, Map novosDados) {
        Empresa empresaAtual = buscarEmpresa(id)
        if (empresaAtual == null) {
            throw new IllegalArgumentException("Erro: Empresa com ID $id não encontrada para atualização.")
        }

        novosDados.each { key, value ->
            if (empresaAtual.hasProperty(key) && key != 'class' && key != 'metaClass' && key != 'vagas') {
                try {
                    empresaAtual."$key" = value
                } catch (ReadOnlyPropertyException roe) {
                }
            }
        }

        validarEmpresa(empresaAtual)

        if (novosDados.containsKey("vagas") && novosDados.get("vagas") != null) {
            List<Map> dadosNovasVagas = (List<Map>) novosDados.get("vagas")
            List<Vaga> vagasProcessadas = []

            dadosNovasVagas.each { mapVaga ->
                Vaga vagaNova = new Vaga(mapVaga)
                vagaNova.empresa = empresaAtual
                validarVaga(vagaNova)
                repository.adicionarVaga(vagaNova)
                vagasProcessadas.add(vagaNova)
            }
            empresaAtual.vagas = vagasProcessadas
        }

        repository.atualizarEmpresa(id, empresaAtual)
        return empresaAtual
    }
}