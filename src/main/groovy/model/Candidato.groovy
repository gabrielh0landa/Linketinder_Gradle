package model

import groovy.transform.ToString

import java.time.LocalDate

@ToString(includeSuper = true, includeNames = true)
class Candidato extends Pessoa {

    String sobrenome
    String cpf
    LocalDate dataDeNascimento
    List<String> competencias = new ArrayList<String>()

    Candidato(Integer id, String nome, String email, String descricao, String cep, String pais, String senha, List<String> competencias, String cpf, LocalDate dataDeNascimento, String sobrenome) {
        super(id, nome, email, descricao, cep, pais, senha)
        this.competencias = competencias
        this.cpf = cpf
        this.dataDeNascimento = dataDeNascimento
        this.sobrenome = sobrenome
    }

    Candidato() {}
}