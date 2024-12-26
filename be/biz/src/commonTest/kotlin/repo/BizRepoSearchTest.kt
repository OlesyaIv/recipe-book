package repo

import RecipeBookContext
import RecipeBookCorSettings
import RecipeProcessor
import RecipeRepositoryMock
import kotlinx.coroutines.test.runTest
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeFilter
import models.RecipeId
import models.RecipeIngredient
import models.RecipeUserId
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = RecipeUserId("321")
    private val command = RecipeBookCommand.SEARCH
    private val initRecipe = Recipe(
        id = RecipeId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
    )
    private val repo = RecipeRepositoryMock(
        invokeSearchRecipe = {
            DbRecipesResponseOk(
                data = listOf(initRecipe),
            )
        }
    )
    private val settings = RecipeBookCorSettings(repoTest = repo)
    private val processor = RecipeProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = RecipeBookContext(
            command = command,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.TEST,
            recipeFilterRequest = RecipeFilter(
                searchString = "abc",
            ),
        )
        processor.exec(ctx)
        assertEquals(RecipeBookState.FINISHING, ctx.state)
        assertEquals(1, ctx.recipesResponse.size)
    }
}
