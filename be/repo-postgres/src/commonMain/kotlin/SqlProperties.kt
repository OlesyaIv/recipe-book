package olesyaiv.recipebook.be.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "postgres",
    val database: String = "education_recipes",
    val schema: String = "public",
    val table: String = "recipes",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
