package repo

import exceptions.RepoConcurrencyException
import exceptions.RepoException
import helpers.errorSystem
import models.Recipe
import models.RecipeError
import models.RecipeId
import models.RecipeLock

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: RecipeId) = DbRecipeResponseErr(
    RecipeError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbRecipeResponseErr(
    RecipeError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldRecipe: Recipe,
    expectedLock: RecipeLock,
    exception: Exception = RepoConcurrencyException(
        id = oldRecipe.id,
        expectedLock = expectedLock,
        actualLock = oldRecipe.lock,
    ),
) = DbRecipeResponseErrWithData(
    recipe = oldRecipe,
    err = RecipeError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldRecipe.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: RecipeId) = DbRecipeResponseErr(
    RecipeError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Recipe ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbRecipeResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
