package model

import groovy.transform.ToString

@ToString(includeNames = true, includePackage = false, excludes = 'empresa')
class Vaga {

    Integer id
    String nome
    String descricao
    String local
    Empresa empresa
    List<String> competencias = new ArrayList<String>()

    Vaga(Integer id, String nome, String descricao, String local, Empresa empresa, List<String> competencias) {
        this.id = id
        this.nome = nome
        this.descricao = descricao
        this.local = local
        this.empresa = empresa
        this.competencias = competencias
    }

    Vaga(){

    }
}