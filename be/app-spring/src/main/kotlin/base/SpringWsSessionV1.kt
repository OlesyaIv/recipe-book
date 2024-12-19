package olesyaiv.recipebook.spring.base

import apiV1ResponseSerialize
import olesyaiv.edu.api.v1.models.IResponse
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import ws.IRecipeBookWsSession

data class SpringWsSessionV1(
    private val session: WebSocketSession,
) : IRecipeBookWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        val message = apiV1ResponseSerialize(obj)
        println("SENDING to WsV1: $message")
        session.send(Mono.just(session.textMessage(message)))
    }
}
