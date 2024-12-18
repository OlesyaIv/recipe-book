package config

import IAppSettings
import RecipeProcessor

data class AppSettings(
    override val processor: RecipeProcessor
): IAppSettings
