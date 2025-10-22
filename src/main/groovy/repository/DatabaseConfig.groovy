package repository

import groovy.sql.Sql

class DatabaseConfig {

    private static final String url = "jdbc:postgresql://localhost:5432/postgres"
    private static final String user = "postgres"
    private static final String pass = "postgres"
    private static final String driver = "org.postgresql.Driver"

    static Sql getSqlInstance() {
        return Sql.newInstance(url, user, pass, driver)
    }
}