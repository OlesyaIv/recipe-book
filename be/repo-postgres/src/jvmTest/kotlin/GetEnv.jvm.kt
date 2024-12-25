package olesyaiv.recipebook.be.repo.postgresql

actual fun getEnv(name: String): String? = System.getenv(name)
