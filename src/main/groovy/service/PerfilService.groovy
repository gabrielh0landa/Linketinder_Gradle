package service

import model.Candidato
import model.Empresa
import model.Vaga
import java.util.Date

class PerfilService {

    Candidato criarNovoCandidato(String nome, String sobrenome, String email, String descricao, String cep, String pais, String senha, List<String> competencias, String cpf, Date dataNascimento) {
        return new Candidato(null, nome, email, descricao, cep, pais, senha, competencias, cpf, dataNascimento, sobrenome)
    }

    void atualizarPerfilCandidato(Candidato candidato, String novoNome, String novoSobrenome, String novoCpf, Date novaDataNascimento, String novoEmail, String novaDescricao, String novoCep, String novoPais, List<String> novasCompetencias) {
        if (novoNome) candidato.nome = novoNome
        if (novoSobrenome) candidato.sobrenome = novoSobrenome
        if (novoCpf) candidato.cpf = novoCpf
        if (novaDataNascimento) candidato.data_nascimento = novaDataNascimento
        if (novoEmail) candidato.email = novoEmail
        if (novaDescricao) candidato.descricao = novaDescricao
        if (novoCep) candidato.cep = novoCep
        if (novoPais) candidato.pais = novoPais
        if (novasCompetencias) candidato.competencias.addAll(novasCompetencias)
    }

    Empresa criarNovaEmpresa(String nome, String cnpj, String email, String descricao, String cep, String pais, String senha, List<String> competencias) {
        return new Empresa(null, nome, email, descricao, cep, pais, senha, competencias, cnpj)
    }

    void atualizarPerfilEmpresa(Empresa empresa, String novoNome, String novoCnpj, String novoEmail, String novaDescricao, String novoCep, String novoPais, List<String> novasCompetencias) {
        if (novoNome) empresa.nome = novoNome
        if (novoCnpj) empresa.cnpj = novoCnpj
        if (novoEmail) empresa.email = novoEmail
        if (novaDescricao) empresa.descricao = novaDescricao
        if (novoCep) empresa.cep = novoCep
        if (novoPais) empresa.pais = novoPais
        if (novasCompetencias) empresa.competencias.addAll(novasCompetencias)
    }

    Vaga criarNovaVaga(String nome, String descricao, String local, Integer idEmpresa, List<String> competencias) {
        return new Vaga(null, nome, descricao, local, idEmpresa, competencias)
    }
}