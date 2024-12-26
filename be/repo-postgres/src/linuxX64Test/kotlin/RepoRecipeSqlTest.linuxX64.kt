package olesyaiv.recipebook.be.repo.postgresql

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.test.runTest
import models.Recipe
import models.RecipeLock
import models.RecipeUserId
import platform.posix.getenv
import repo.DbRecipeFilterRequest
import repo.DbRecipeRequest
import kotlin.test.Test

class RepoRecipeSqlTest {
    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun create() = runTest {
        val pgPort = getenv("postgresPort")?.toKString()?.toIntOrNull() ?: 5432

        val repo = RepoRecipeSql(
            properties = SqlProperties(port = pgPort)
        )
        val res = repo.createRecipe(
            rq = DbRecipeRequest(
                Recipe(
                    title = "tttt",
                    description = "zzzz",
                    ownerId = RecipeUserId("1234"),
                    ingredients = mapOf(),
                    lock = RecipeLock("235356"),
                )
            )
        )
        println("CREATED $res")
    }

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun search() = runTest {
        val pgPort = getenv("postgresPort")?.toKString()?.toIntOrNull() ?: 5432

        val repo = RepoRecipeSql(
            properties = SqlProperties(port = pgPort)
        )
        val res = repo.searchRecipe(
            rq = DbRecipeFilterRequest(
                nameFilter = "tttt",
                ownerId = RecipeUserId("1234"),
            )
        )
        println("SEARCH $res")
    }
}
