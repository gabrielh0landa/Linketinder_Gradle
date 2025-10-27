package model

import groovy.transform.ToString

@ToString
class Pessoa {
    Integer id
    String nome
    String email
    String descricao
    String cep
    String pais
    String senha
    List<String> competencias = []

    // Construtor com ID
    Pessoa(Integer id, String nome, String email, String descricao, String cep, String pais, String senha, List<String> competencias = []) {
        this.id = id
        this.nome = nome
        this.email = email
        this.descricao = descricao
        this.cep = cep
        this.pais = pais
        this.senha = senha
        this.competencias = competencias
    }
    // Construtor sem ID
    Pessoa(String nome, String email, String descricao, String cep, String pais, String senha, List<String> competencias = []) {
        this.nome = nome
        this.email = email
        this.descricao = descricao
        this.cep = cep
        this.pais = pais
        this.senha = senha
        this.competencias = competencias
    }

    Pessoa() {}

    void setCompetencias(String competencia) {
        this.competencias << competencia
    }

    void exibirPerfil() {
        println this
    }
}