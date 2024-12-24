package olesyaiv.recipebook.app.spring.base

import ws.IRecipeBookWsSession
import ws.IRecipeBookWsSessionRepo

class SpringWsSessionRepo: IRecipeBookWsSessionRepo {
    private val sessions: MutableSet<IRecipeBookWsSession> = mutableSetOf()
    override fun add(session: IRecipeBookWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IRecipeBookWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}
