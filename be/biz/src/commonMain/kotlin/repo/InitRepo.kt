package repo

import RecipeBookContext
import exceptions.RecipeDbNotConfiguredException
import helpers.errorSystem
import helpers.fail
import models.RecipeBookWorkMode
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        recipeRepo = when (workMode) {
            RecipeBookWorkMode.TEST -> corSettings.repoTest
            RecipeBookWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != RecipeBookWorkMode.STUB && recipeRepo == IRepoRecipe.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = RecipeDbNotConfiguredException(workMode)
            )
        )
    }
}
