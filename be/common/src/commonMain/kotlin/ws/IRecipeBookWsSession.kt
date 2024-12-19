package ws

interface IRecipeBookWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : IRecipeBookWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}
