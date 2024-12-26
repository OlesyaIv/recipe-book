package olesyaiv.recipebook.app.spring.repo

import RecipeRepoInMemory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import repo.IRepoRecipe

@TestConfiguration
class RepoInMemoryConfig {
    @Suppress("unused")
    @Bean
    @Primary
    fun prodRepo(): IRepoRecipe = RecipeRepoInMemory()
}
