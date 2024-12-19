package olesyaiv.recipebook.spring.controllers

import RecipeBookContext
import apiV1Mapper
import controllerHelper
import fromTransport
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import models.RecipeBookCommand
import olesyaiv.edu.api.v1.models.IRequest
import olesyaiv.recipebook.spring.base.AppSettings
import olesyaiv.recipebook.spring.base.SpringWsSessionV1
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import toTransportRecipe

@Component
class RecipeControllerV1Ws(
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
        sess.send(messageObj)

        val messages = session.receive().asFlow()
            .map { message ->
                process {
                    wsSession = sess
                    val request = apiV1Mapper.readValue(message.payloadAsText, IRequest::class.java)
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
            .map { session.textMessage(apiV1Mapper.writeValueAsString(it)) }
            .asFlux()
        session.send(output)
    }

    private suspend fun process(function: RecipeBookContext.() -> Unit) = appSettings.controllerHelper(
        getRequest = function,
        toResponse = RecipeBookContext::toTransportRecipe
    )
}
