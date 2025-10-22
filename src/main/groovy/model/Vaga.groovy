package model

import groovy.transform.ToString

@ToString(includeNames = true, includePackage = false)
class Vaga {

    Integer id
    String nome
    String descricao
    String local
    Integer idEmpresa
    List<String> competencias = []

    Vaga(Integer id, String nome, String descricao, String local, Integer idEmpresa, List<String> competencias) {
        this.id = id
        this.nome = nome
        this.descricao = descricao
        this.local = local
        this.idEmpresa = idEmpresa
        this.competencias = competencias
    }

}