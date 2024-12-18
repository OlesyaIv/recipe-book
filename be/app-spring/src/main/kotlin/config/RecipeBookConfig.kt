package config

import RecipeProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
class RecipeBookConfig {
    @Bean
    fun processor() = RecipeProcessor()

    @Bean
    fun appSettings(
        processor: RecipeProcessor,
    ) = AppSettings(
        processor = processor,
    )
}
