package main

import controller.PerfilController
import repository.IRepository
import repository.RepositoryJdbc
import repository.DatabaseConfig
import java.util.Scanner

class Master {

    static void main(String[] args) {

        println "Tentando conectar ao banco de dados..."
        try {
            def sql = DatabaseConfig.getSqlInstance()
            sql.firstRow("SELECT 1")
            println ">>> SUCESSO: Conexão com o banco de dados estabelecida."
            sql.close()
        } catch (Exception e) {
            println ">>> ERRO: Falha ao conectar com o banco de dados."
            println "Verifique se o PostgreSQL está rodando e se as credenciais em DatabaseConfig.groovy estão corretas."
            e.printStackTrace()
            return
        }


        Scanner sc = new Scanner(System.in)
        IRepository repo = new RepositoryJdbc()
        def controller = new PerfilController(repo)

        controller.exibirMenu(sc)
    }
}