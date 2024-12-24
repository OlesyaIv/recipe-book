import models.Recipe
import models.RecipeId
import models.RecipeIngredient
import models.RecipeUserId

object RecipeStubHoneyCake {
    val STUB_HONEY_CAKE: Recipe
        get() = Recipe(
            id = RecipeId("666"),
            title = "Honey Cake",
            description = "tasty cake",
            ownerId = RecipeUserId("user-1"),
            ingredients = mapOf(
                RecipeIngredient.EGG to 2.0F
            )
        )
}
