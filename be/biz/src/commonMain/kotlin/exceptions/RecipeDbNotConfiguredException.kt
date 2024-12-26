package exceptions

import models.RecipeBookWorkMode

class RecipeDbNotConfiguredException(val workMode: RecipeBookWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
