package validation

import models.RecipeBookCommand
import kotlin.test.Test

class BizValidationUpdateTest: BaseBizValidationTest() {
    override val command = RecipeBookCommand.UPDATE

    @Test fun correctTitle() = validationNameCorrect(command, processor)
    @Test fun trimTitle() = validationNameTrim(command, processor)
    @Test fun emptyTitle() = validationNameEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationNameSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)
}
