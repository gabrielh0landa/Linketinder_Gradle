package model

import groovy.transform.ToString

@ToString(includeSuper = true, includeNames = true)
class Empresa extends Pessoa {

    String cnpj

    Empresa(Integer id, String nome, String email, String descricao, String cep, String pais, String senha, List<String> competencias = [], String cnpj) {
        super(id, nome, email, descricao, cep, pais, senha, competencias)
        this.cnpj = cnpj
    }

    Empresa() {}
}