package olesyaiv.recipebook.app.spring.config

import olesyaiv.recipebook.be.repo.postgresql.SqlProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "psql")
data class RecipeBookConfigPostgres(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "postgres",
    var database: String = "education_recipes",
    var schema: String = "public",
    var table: String = "recipes",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}
