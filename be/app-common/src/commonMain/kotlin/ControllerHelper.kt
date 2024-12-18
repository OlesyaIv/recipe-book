import helpers.asRecipeError
import kotlinx.datetime.Clock
import models.RecipeBookState

suspend inline fun <T> IAppSettings.controllerHelper(
    crossinline getRequest: suspend RecipeBookContext.() -> Unit,
    crossinline toResponse: suspend RecipeBookContext.() -> T
): T {
    val ctx = RecipeBookContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        processor.exec(ctx)
        ctx.toResponse()
    } catch (e: Throwable) {
        ctx.state = RecipeBookState.FAILING
        ctx.errors.add(e.asRecipeError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}
