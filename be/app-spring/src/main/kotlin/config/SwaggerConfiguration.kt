package olesyaiv.recipebook.app.spring.config

import org.springdoc.core.configuration.SpringDocConfiguration
import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.providers.ObjectMapperProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
class SwaggerConfiguration {
    @Bean
    fun springDocConfiguration() = SpringDocConfiguration()

    @Bean
    fun springDocConfigProperties() = SpringDocConfigProperties()

    @Bean
    fun objectMapperProvider(springDocConfigProperties: SpringDocConfigProperties) =
        ObjectMapperProvider(springDocConfigProperties)
}
