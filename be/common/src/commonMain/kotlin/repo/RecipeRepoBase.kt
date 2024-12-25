package repo

import helpers.errorSystem

abstract class RecipeRepoBase: IRepoRecipe {

    protected suspend fun tryRecipeMethod(block: suspend () -> IDbRecipeResponse) = try {
        block()
    } catch (e: Throwable) {
        DbRecipeResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryRecipesMethod(block: suspend () -> IDbRecipesResponse) = try {
        block()
    } catch (e: Throwable) {
        DbRecipesResponseErr(errorSystem("methodException", e = e))
    }
}
