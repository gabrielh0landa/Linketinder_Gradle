package repository

import groovy.sql.Sql

class DatabaseConfig {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres"
    private static final String USER = "postgres"
    private static final String PASS = "postgres"
    private static final String DRIVER = "org.postgresql.Driver"

    static Sql getSqlInstance() {
        return Sql.newInstance(DB_URL, USER, PASS, DRIVER)
    }
}