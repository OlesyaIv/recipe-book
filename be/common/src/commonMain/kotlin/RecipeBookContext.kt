import NONE
import kotlinx.datetime.Instant
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookRequestId
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeError
import models.RecipeFilter
import stubs.Stubs
import ws.IRecipeBookWsSession

data class RecipeBookContext(
    var command: RecipeBookCommand = RecipeBookCommand.NONE,
    var state: RecipeBookState = RecipeBookState.NONE,
    val errors: MutableList<RecipeError> = mutableListOf(),

    var workMode: RecipeBookWorkMode = RecipeBookWorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,

    var wsSession: IRecipeBookWsSession = IRecipeBookWsSession.NONE,

    var requestId: RecipeBookRequestId = RecipeBookRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var recipeRequest: Recipe = Recipe(),
    var recipeFilterRequest: RecipeFilter = RecipeFilter(),

    var recipeResponse: Recipe = Recipe(),
    var recipesResponse: MutableList<Recipe> = mutableListOf(),

    var recipeValidating: Recipe = Recipe(),
    var recipeFilterValidating: RecipeFilter = RecipeFilter(),

    var recipeValidated: Recipe = Recipe(),
    var recipeFilterValidated: RecipeFilter = RecipeFilter(),
)
