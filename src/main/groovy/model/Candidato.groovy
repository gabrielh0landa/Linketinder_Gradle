package model

import groovy.transform.ToString

import java.time.LocalDate

@ToString(includeSuper = true, includeNames = true)
class Candidato extends Pessoa {

    String sobrenome
    String cpf
    LocalDate data_nascimento

    // Construtor da Super Classe com ID
    Candidato(Integer id, String nome, String email, String descricao, String cep, String pais, String senha, List<String> competencias = [], String cpf, LocalDate data_nascimento, String sobrenome) {
        super(id, nome, email, descricao, cep, pais, senha, competencias)
        this.cpf = cpf
        this.data_nascimento = data_nascimento
        this.sobrenome = sobrenome
    }

    Candidato() {}
}