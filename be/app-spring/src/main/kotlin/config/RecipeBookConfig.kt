package olesyaiv.recipebook.app.spring.config

import RecipeBookCorSettings
import RecipeProcessor
import RecipeRepoInMemory
import RecipeRepoStub
import olesyaiv.recipebook.app.spring.base.AppSettings
import olesyaiv.recipebook.app.spring.base.SpringWsSessionRepo
import olesyaiv.recipebook.be.repo.postgresql.RepoRecipeSql
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import repo.IRepoRecipe

@Suppress("unused")
@EnableConfigurationProperties(RecipeBookConfigPostgres::class)
@Configuration
class RecipeBookConfig(
    val postgresConfig: RecipeBookConfigPostgres
) {
    @Bean
    fun processor(corSettings: RecipeBookCorSettings) = RecipeProcessor(corSettings = corSettings)

    @Bean
    fun wsRepo(): SpringWsSessionRepo = SpringWsSessionRepo()

    @Bean
    fun testRepo(): IRepoRecipe = RecipeRepoInMemory()

    @Bean
    fun prodRepo(): IRepoRecipe = RepoRecipeSql(postgresConfig.psql)

    @Bean
    fun stubRepo(): IRepoRecipe = RecipeRepoStub()

    @Bean
    @Primary
    fun corSettings(): RecipeBookCorSettings = RecipeBookCorSettings(
        wsSessions = wsRepo(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo()
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
