package validation

import models.RecipeBookCommand
import kotlin.test.Test

class BizValidationCreateTest: BaseBizValidationTest() {
    override val command: RecipeBookCommand = RecipeBookCommand.CREATE

    @Test fun correctName() = validationNameCorrect(command, processor)
    @Test fun trimName() = validationNameTrim(command, processor)
    @Test fun emptyName() = validationNameEmpty(command, processor)
    @Test fun badSymbolsName() = validationNameSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)
}
