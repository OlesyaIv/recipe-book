package exceptions

import models.RecipeId

class RepoEmptyLockException(id: RecipeId): RepoRecipeException(
    id,
    "Lock is empty in DB"
)
