
import general.initStatus
import general.operation
import models.RecipeBookCommand
import models.RecipeId
import models.RecipeLock
import stubs.*
import olesyaiv.recipebook.lib.cor.rootChain
import olesyaiv.recipebook.lib.cor.worker
import validation.*

const val stubsHandlingTitle = "Обработка стабов"
const val successTitle = "Имитация успешной обработки"
const val dbErrorTitle = "Имитация ошибки работы с БД"
const val validationErrorTitle = "Имитация ошибки валидации "
const val stubIsUnacceptable = "Ошибка: запрошенный стаб недопустим"
const val nonEmptyFieldCheckTitle = "Проверка на непустой "
const val idFormatCheckTitle = "Проверка формата id"
const val successValidationTitle = "Успешное завершение процедуры валидации"
const val copyToContextTitle = "Копируем поля в recipeValidating"
const val clearFieldTitle = "Очистка "
const val notEmptyFieldTitle = "Проверка, что %s не пусто: "
const val symbolsCheckTitle = "Проверка символов"

@Suppress("unused", "RedundantSuspendModifier")
class RecipeProcessor {

    suspend fun exec(ctx: RecipeBookContext) = businessChain.exec(ctx)

    private val businessChain = rootChain {
        initStatus("Инициализация статуса")

        operation("Создание", RecipeBookCommand.CREATE) {
            stubs(stubsHandlingTitle) {
                stubCreateSuccess(successTitle)
                stubValidationBadName(validationErrorTitle + "name")
                stubValidationBadDescription(validationErrorTitle + "description")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker(copyToContextTitle) { recipeValidating = recipeRequest.deepCopy() }
                worker(clearFieldTitle + "id") { recipeValidating.id = RecipeId.NONE }
                worker(clearFieldTitle + "name") { recipeValidating.title = recipeValidating.title.trim() }
                worker(clearFieldTitle + "description") { recipeValidating.description = recipeValidating.description.trim() }
                validateNameNotEmpty(notEmptyFieldTitle + "name")
                validateNameHasContent(symbolsCheckTitle)
                validateDescriptionNotEmpty(notEmptyFieldTitle + "description")
                validateDescriptionHasContent(symbolsCheckTitle)
                finishRecipeValidation(successValidationTitle)
            }
        }
        operation("Получение", RecipeBookCommand.READ) {
            stubs(stubsHandlingTitle) {
                stubReadSuccess(successTitle)
                stubValidationBadId(validationErrorTitle + "id")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker(copyToContextTitle) { recipeValidating = recipeRequest.deepCopy() }
                worker(clearFieldTitle + "id") { recipeValidating.id = RecipeId(recipeValidating.id.asString().trim()) }
                validateIdNotEmpty(nonEmptyFieldCheckTitle + "id")
                validateIdProperFormat(idFormatCheckTitle)
                finishRecipeValidation(successValidationTitle)
            }
        }
        operation("Изменение", RecipeBookCommand.UPDATE) {
            stubs(stubsHandlingTitle) {
                stubUpdateSuccess(successTitle)
                stubValidationBadId(validationErrorTitle + "id")
                stubValidationBadName(validationErrorTitle + "name")
                stubValidationBadDescription(validationErrorTitle + "description")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker(copyToContextTitle) { recipeValidating = recipeRequest.deepCopy() }
                worker(clearFieldTitle + "id") { recipeValidating.id = RecipeId(recipeValidating.id.asString().trim()) }
                worker(clearFieldTitle + "lock") { recipeValidating.lock = RecipeLock(recipeValidating.lock.asString().trim()) }
                worker(clearFieldTitle + "name") { recipeValidating.title = recipeValidating.title.trim() }
                worker(clearFieldTitle + "description") { recipeValidating.description = recipeValidating.description.trim() }
                validateIdNotEmpty(nonEmptyFieldCheckTitle + "id")
                validateIdProperFormat(idFormatCheckTitle)
                validateLockNotEmpty(nonEmptyFieldCheckTitle + "lock")
                validateLockProperFormat("Проверка формата lock")
                validateNameNotEmpty(nonEmptyFieldCheckTitle + "name")
                validateNameHasContent("Проверка на наличие содержания в name")
                validateDescriptionNotEmpty(nonEmptyFieldCheckTitle + "description")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")
                finishRecipeValidation(successValidationTitle)
            }
        }
        operation("Удаление", RecipeBookCommand.DELETE) {
            stubs(stubsHandlingTitle) {
                stubDeleteSuccess(successTitle)
                stubValidationBadId(validationErrorTitle + "id")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker(copyToContextTitle) {
                    recipeValidating = recipeRequest.deepCopy()
                }
                worker(clearFieldTitle + "id") { recipeValidating.id = RecipeId(recipeValidating.id.asString().trim()) }
                worker(clearFieldTitle + "lock") { recipeValidating.lock = RecipeLock(recipeValidating.lock.asString().trim()) }
                validateIdNotEmpty(nonEmptyFieldCheckTitle + "id")
                validateIdProperFormat(idFormatCheckTitle)
                validateLockNotEmpty(nonEmptyFieldCheckTitle + "lock")
                validateLockProperFormat("Проверка формата lock")
                finishRecipeValidation(successValidationTitle)
            }
        }
        operation("Поиск", RecipeBookCommand.SEARCH) {
            stubs(stubsHandlingTitle) {
                stubSearchSuccess(successTitle)
                stubValidationBadId(validationErrorTitle + "id")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker("Копируем поля в recipeFilterValidating") { recipeFilterValidating = recipeFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")
                finishRecipeFilterValidation(successValidationTitle)
            }
        }
        operation("Стоимость ингредиентов", RecipeBookCommand.COST) {
            stubs(stubsHandlingTitle) {
                stubReportSuccess(successTitle)
                stubValidationBadId(validationErrorTitle + "id")
                stubDbError(dbErrorTitle)
                stubNoCase(stubIsUnacceptable)
            }
            validation {
                worker(copyToContextTitle) { recipeValidating = recipeRequest.deepCopy() }
                worker(clearFieldTitle + "id") { recipeValidating.id = RecipeId(recipeValidating.id.asString().trim()) }
                validateIdNotEmpty(nonEmptyFieldCheckTitle + "id")
                validateIdProperFormat(idFormatCheckTitle)
                finishRecipeValidation(successValidationTitle)
            }
        }
    }.build()
}
