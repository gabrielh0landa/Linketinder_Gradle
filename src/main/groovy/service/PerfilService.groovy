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
            // Silenciosamente aceita
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
                    // Ignora
                }
            }
        }

        validarCandidato(candidatoAtual)
        repository.atualizarCandidato(id, candidatoAtual)
        return candidatoAtual
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
            // Aceita
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
                    // Ignora
                }
            }
        }

        validarEmpresa(empresaAtual)

        if (novosDados.containsKey("vagas") && novosDados.get("vagas") != null) {
            List<Map> dadosNovasVagas = (List<Map>) novosDados.get("vagas")
            List<Vaga> vagasProcessadas = []

            // ATENÇÃO: Descomente a linha abaixo QUANDO implementar deletarVagasPorEmpresa no Repository
            // repository.deletarVagasPorEmpresa(id)

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