package exceptions

import models.RecipeId
import models.RecipeLock

class RepoConcurrencyException(id: RecipeId, expectedLock: RecipeLock, actualLock: RecipeLock?): RepoRecipeException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
