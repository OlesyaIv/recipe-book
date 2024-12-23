package ws

interface IRecipeBookWsSessionRepo  {
    fun add(session: IRecipeBookWsSession)
    fun clearAll()
    fun remove(session: IRecipeBookWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IRecipeBookWsSessionRepo {
            override fun add(session: IRecipeBookWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IRecipeBookWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}
