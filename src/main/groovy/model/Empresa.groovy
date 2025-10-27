package model

import groovy.transform.ToString

@ToString(includeSuper = true, includeNames = true)
class Empresa extends Pessoa {

    String cnpj
    List<Vaga> vagas = []


    Empresa(Integer id, String nome, String email, String descricao, String cep, String pais, String senha, List<String> competencias = [], String cnpj, List<Vaga> vagas = []) {
        super(id, nome, email, descricao, cep, pais, senha, competencias)
        this.cnpj = cnpj
        this.vagas = vagas
    }

    Empresa() {}
}