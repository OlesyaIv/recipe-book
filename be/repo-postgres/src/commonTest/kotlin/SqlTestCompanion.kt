package olesyaiv.recipebook.be.repo.postgresql

import IRepoRecipeInitializable
import RecipeRepoInitialized
import com.benasher44.uuid.uuid4
import models.Recipe
import olesyaiv.recipebook.be.repo.postgresql.RepoRecipeSql
import olesyaiv.recipebook.be.repo.postgresql.SqlProperties
import olesyaiv.recipebook.be.repo.postgresql.getEnv

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "postgres"
    val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<Recipe> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): IRepoRecipeInitializable = RecipeRepoInitialized(
        repo = RepoRecipeSql(
            SqlProperties(
                host = HOST,
                user = USER,
                password = PASS,
                port = PORT,
            ),
            randomUuid = randomUuid
        ),
        initObjects = initObjects,
    )
}
