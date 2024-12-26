import general.initStatus
import general.operation
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeId
import models.RecipeLock
import olesyaiv.recipebook.lib.cor.chain
import stubs.*
import olesyaiv.recipebook.lib.cor.rootChain
import olesyaiv.recipebook.lib.cor.worker
import repo.checkLock
import repo.initRepo
import repo.repoCreate
import repo.repoDelete
import repo.repoPrepareCreate
import repo.repoPrepareDelete
import repo.repoPrepareUpdate
import repo.repoRead
import repo.repoSearch
import repo.repoUpdate
import repo.prepareResult
import repo.repoPrepareCost
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

class RecipeProcessor(
    private val corSettings: RecipeBookCorSettings = RecipeBookCorSettings.NONE
) {

    suspend fun exec(ctx: RecipeBookContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<RecipeBookContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

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
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание в БД")
            }
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика чтения"
                repoRead("Чтение из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == RecipeBookState.RUNNING }
                    handle { recipeRepoDone = recipeRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика сохранения"
                repoRead("Чтение из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление в БД")
            }
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика удаления"
                repoRead("Чтение из БД")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление из БД")
            }
            prepareResult("Подготовка ответа")
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
            repoSearch("Поиск в БД по фильтру")
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика поиска в БД"
                repoRead("Чтение из БД")
                repoPrepareCost("Подготовка стоимости ингредиентов")
            }
            prepareResult("Подготовка ответа")
        }
    }.build()
}
