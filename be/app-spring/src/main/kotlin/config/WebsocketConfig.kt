package olesyaiv.recipebook.spring.config

import olesyaiv.recipebook.spring.controllers.RecipeControllerV1Ws
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler

@Suppress("unused")
@Configuration
class WebSocketConfig(
    private val recipeControllerV1: RecipeControllerV1Ws,
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMap: Map<String, WebSocketHandler> = mapOf(
            "/v1/ws" to recipeControllerV1,
        )
        return SimpleUrlHandlerMapping(handlerMap, 1)
    }
}
