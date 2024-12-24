package olesyaiv.recipebook.e2e.be.fixture.client

interface Client {

    suspend fun sendAndReceive(version: String, path: String, request: String): String
}

