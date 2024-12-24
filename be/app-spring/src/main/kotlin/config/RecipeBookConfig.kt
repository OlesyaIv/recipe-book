package olesyaiv.recipebook.app.spring.config

import RecipeBookCorSettings
import RecipeProcessor
import olesyaiv.recipebook.app.spring.base.AppSettings
import olesyaiv.recipebook.app.spring.base.SpringWsSessionRepo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
class RecipeBookConfig {
    @Bean
    fun processor() = RecipeProcessor()

    @Bean
    fun wsRepo(): SpringWsSessionRepo = SpringWsSessionRepo()

    @Bean
    fun corSettings(): RecipeBookCorSettings = RecipeBookCorSettings(
        wsSessions = wsRepo()
    )

    @Bean
    fun appSettings(
        corSettings: RecipeBookCorSettings,
        processor: RecipeProcessor,
    ) = AppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
