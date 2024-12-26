import models.Recipe
import models.RecipeId
import models.RecipeIngredient
import models.RecipeLock
import models.RecipeUserId

abstract class BaseInitRecipes(private val op: String): IInitObjects<Recipe> {
    open val lockOld: RecipeLock = RecipeLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: RecipeLock = RecipeLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: RecipeUserId = RecipeUserId("owner-123"),
        lock: RecipeLock = lockOld,
    ) = Recipe(
        id = RecipeId("recipe-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
        lock = lock,
    )
}
