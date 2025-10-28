package model

import groovy.transform.ToString

@ToString
class Pessoa {
    int id
    String nome
    String email
    String descricao
    String cep
    String pais
    String senha

    Pessoa(Integer id, String nome, String email, String descricao, String cep, String pais, String senha) {
        this.id = id
        this.nome = nome
        this.email = email
        this.descricao = descricao
        this.cep = cep
        this.pais = pais
        this.senha = senha

    }

    Pessoa() {}

    void exibirPerfil() {
        println this
    }
}