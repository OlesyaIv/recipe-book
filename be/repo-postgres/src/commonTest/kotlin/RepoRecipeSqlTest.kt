package olesyaiv.recipebook.be.repo.postgresql

import IRepoRecipeInitializable
import RecipeRepoInitialized
import RepoRecipeCreateTest
import RepoRecipeDeleteTest
import RepoRecipeReadTest
import RepoRecipeSearchTest
import RepoRecipeUpdateTest
import repo.IRepoRecipe
import kotlin.test.AfterTest

private fun IRepoRecipe.clear() {
    val pgRepo = (this as RecipeRepoInitialized).repo as RepoRecipeSql
    pgRepo.clear()
}

class RepoRecipeSQLCreateTest : RepoRecipeCreateTest() {
    override val repo: IRepoRecipeInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { uuidNew.asString() },
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoRecipeSQLReadTest : RepoRecipeReadTest() {
    override val repo: IRepoRecipe = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLUpdateTest : RepoRecipeUpdateTest() {
    override val repo: IRepoRecipe = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoAdSQLDeleteTest : RepoRecipeDeleteTest() {
    override val repo: IRepoRecipe = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLSearchTest : RepoRecipeSearchTest() {
    override val repo: IRepoRecipe = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}
