package olesyaiv.recipebook.app.spring.controllers

import RecipeBookContext
import controllerHelper
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import models.RecipeBookCommand
import olesyaiv.recipebook.api.v2.models.IRequest
import olesyaiv.recipebook.api.v2.apiV2RequestDeserialize
import olesyaiv.recipebook.api.v2.apiV2ResponseSerialize
import olesyaiv.recipebook.app.spring.base.AppSettings
import olesyaiv.recipebook.app.spring.base.SpringWsSessionV1
import olesyaiv.recipebook.api.v2.mappers.fromTransport
import olesyaiv.recipebook.api.v2.mappers.toTransportRecipe
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class RecipeControllerV2Ws(
    private val appSettings: AppSettings
) : WebSocketHandler {
    private val sessions = appSettings.corSettings.wsSessions

    override fun handle(session: WebSocketSession): Mono<Void> = runBlocking {
        val sess = SpringWsSessionV1(session)
        sessions.add(sess)
        val messageObj = process {
            command = RecipeBookCommand.INIT
            wsSession = sess
        }

        val messages = session.receive().asFlow()
            .map { message ->
                process {
                    wsSession = sess
                    val request = apiV2RequestDeserialize<IRequest>(message.payloadAsText)
                    fromTransport(request)
                }
            }

        val output = merge(flowOf(messageObj), messages)
            .onCompletion {
                process {
                    wsSession = sess
                    command = RecipeBookCommand.FINISH
                }
                sessions.remove(sess)
            }
            .map { session.textMessage(apiV2ResponseSerialize(it)) }
            .asFlux()
        session.send(output)
    }

    private suspend fun process(function: RecipeBookContext.() -> Unit) = appSettings.controllerHelper(
        getRequest = function,
        toResponse = RecipeBookContext::toTransportRecipe
    )
}
