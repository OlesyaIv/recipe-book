package validation

import RecipeBookContext
import helpers.errorValidation
import helpers.fail
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.validateNameHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в name.
        Отказываем в create name, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { recipeValidating.title.isNotEmpty() && ! recipeValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "name",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
