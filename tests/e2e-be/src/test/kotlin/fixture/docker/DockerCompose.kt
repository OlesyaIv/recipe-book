package olesyaiv.recipebook.e2e.be.fixture.docker

import io.ktor.http.URLBuilder

interface DockerCompose {
    fun start()
    fun stop()
    fun clearDb()


    val inputUrl: URLBuilder

    val user: String get() = throw UnsupportedOperationException("no user")

    val password: String get() = throw UnsupportedOperationException("no password")
}
