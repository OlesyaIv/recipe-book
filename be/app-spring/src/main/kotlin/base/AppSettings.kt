package olesyaiv.recipebook.spring.base

import IAppSettings
import RecipeBookCorSettings
import RecipeProcessor

data class AppSettings(
    override val corSettings: RecipeBookCorSettings,
    override val processor: RecipeProcessor
): IAppSettings
