package model

import groovy.transform.ToString

@ToString(includeSuper = true, includeNames = true)
class Candidato extends Pessoa {

    String sobrenome
    String cpf
    Date data_nascimento

    Candidato(Integer id, String nome, String email, String descricao, String cep, String pais, String senha, List<String> competencias = [], String cpf, Date data_nascimento, String sobrenome) {
        super(id, nome, email, descricao, cep, pais, senha, competencias)
        this.cpf = cpf
        this.data_nascimento = data_nascimento
        this.sobrenome = sobrenome
    }

    Candidato() {}
}