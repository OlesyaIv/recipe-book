import models.Recipe
import models.RecipeId
import models.RecipeIngredient
import models.RecipeLock
import models.RecipeUserId

data class RecipeEntity(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val ingredients: Map<String, Float>? = null,
    val lock: String? = null,
) {
    constructor(model: Recipe): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        name = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        ingredients = model.ingredients.map { (ingredient, quantity) -> ingredient.name to quantity }.toMap(),
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = Recipe(
        id = id?.let { RecipeId(it) }?: RecipeId.NONE,
        title = name?: "",
        description = description?: "",
        ownerId = ownerId?.let { RecipeUserId(it) }?: RecipeUserId.NONE,
        ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
        lock = lock?.let { RecipeLock(it) } ?: RecipeLock.NONE,
    )
}
