import com.benasher44.uuid.uuid4
import exceptions.RepoEmptyLockException
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import models.Recipe
import models.RecipeId
import models.RecipeLock
import models.RecipeUserId
import repo.DbRecipeFilterRequest
import repo.DbRecipeIdRequest
import repo.DbRecipeRequest
import repo.DbRecipeResponseOk
import repo.DbRecipesResponseOk
import repo.IDbRecipeResponse
import repo.IDbRecipesResponse
import repo.IRepoRecipe
import repo.RecipeRepoBase
import repo.errorDb
import repo.errorEmptyId
import repo.errorEmptyLock
import repo.errorNotFound
import repo.errorRepoConcurrency
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class RecipeRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : RecipeRepoBase(), IRepoRecipe, IRepoRecipeInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, RecipeEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(recipes: Collection<Recipe>) = recipes.map { recipe ->
        val entity = RecipeEntity(recipe)
        require(entity.id != null)
        cache.put(entity.id, entity)
        recipe
    }

    override suspend fun createRecipe(rq: DbRecipeRequest): IDbRecipeResponse = tryRecipeMethod {
        val key = randomUuid()
        val recipe = rq.recipe.copy(id = RecipeId(key), lock = RecipeLock(randomUuid()))
        val entity = RecipeEntity(recipe)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbRecipeResponseOk(recipe)
    }

    override suspend fun readRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse = tryRecipeMethod {
        val key = rq.id.takeIf { it != RecipeId.NONE }?.asString() ?: return@tryRecipeMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbRecipeResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateRecipe(rq: DbRecipeRequest): IDbRecipeResponse = tryRecipeMethod {
        val rqRecipe = rq.recipe
        val id = rqRecipe.id.takeIf { it != RecipeId.NONE } ?: return@tryRecipeMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqRecipe.lock.takeIf { it != RecipeLock.NONE } ?: return@tryRecipeMethod errorEmptyLock(id)

        mutex.withLock {
            val oldRecipe = cache.get(key)?.toInternal()
            when {
                oldRecipe == null -> errorNotFound(id)
                oldRecipe.lock == RecipeLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldRecipe.lock != oldLock -> errorRepoConcurrency(oldRecipe, oldLock)
                else -> {
                    val newRecipe = rqRecipe.copy(lock = RecipeLock(randomUuid()))
                    val entity = RecipeEntity(newRecipe)
                    cache.put(key, entity)
                    DbRecipeResponseOk(newRecipe)
                }
            }
        }
    }


    override suspend fun deleteRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse = tryRecipeMethod {
        val id = rq.id.takeIf { it != RecipeId.NONE } ?: return@tryRecipeMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != RecipeLock.NONE } ?: return@tryRecipeMethod errorEmptyLock(id)

        mutex.withLock {
            val oldRecipe = cache.get(key)?.toInternal()
            when {
                oldRecipe == null -> errorNotFound(id)
                oldRecipe.lock == RecipeLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldRecipe.lock != oldLock -> errorRepoConcurrency(oldRecipe, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbRecipeResponseOk(oldRecipe)
                }
            }
        }
    }

    override suspend fun searchRecipe(rq: DbRecipeFilterRequest): IDbRecipesResponse = tryRecipesMethod {
        val result: List<Recipe> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != RecipeUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.nameFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.name?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbRecipesResponseOk(result)
    }
}
