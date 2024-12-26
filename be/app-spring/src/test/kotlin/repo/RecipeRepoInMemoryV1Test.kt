package olesyaiv.recipebook.app.spring.repo

import RecipeRepoInMemory
import RecipeRepoInitialized
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import olesyaiv.recipebook.app.spring.config.RecipeBookConfig
import olesyaiv.recipebook.app.spring.controllers.RecipeControllerV1Fine
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import repo.DbRecipeFilterRequest
import repo.DbRecipeIdRequest
import repo.DbRecipeRequest
import repo.IRepoRecipe
import kotlin.test.Test

@WebFluxTest(
    RecipeControllerV1Fine::class, RecipeBookConfig::class,
    properties = ["spring.main.allow-bean-definition-overriding=true"]
)
@Import(RepoInMemoryConfig::class)
internal class RecipeRepoInMemoryV1Test : RecipeRepoBaseV1Test() {

    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoRecipe

    @BeforeEach
    fun tearUp() {
        val slotRecipe = slot<DbRecipeRequest>()
        val slotId = slot<DbRecipeIdRequest>()
        val slotFl = slot<DbRecipeFilterRequest>()
        val repo = RecipeRepoInitialized(
            repo = RecipeRepoInMemory(randomUuid = { uuidNew }),
            initObjects = RecipeStub.prepareSearchList("Napoleon") + RecipeStub.get()
        )
        coEvery { testTestRepo.createRecipe(capture(slotRecipe)) } coAnswers { repo.createRecipe(slotRecipe.captured) }
        coEvery { testTestRepo.readRecipe(capture(slotId)) } coAnswers { repo.readRecipe(slotId.captured) }
        coEvery { testTestRepo.updateRecipe(capture(slotRecipe)) } coAnswers { repo.updateRecipe(slotRecipe.captured) }
        coEvery { testTestRepo.deleteRecipe(capture(slotId)) } coAnswers { repo.deleteRecipe(slotId.captured) }
        coEvery { testTestRepo.searchRecipe(capture(slotFl)) } coAnswers { repo.searchRecipe(slotFl.captured) }
    }

    @Test
    override fun createRecipe() = super.createRecipe()

    @Test
    override fun readRecipe() = super.readRecipe()

    @Test
    override fun updateRecipe() = super.updateRecipe()

    @Test
    override fun deleteRecipe() = super.deleteRecipe()

    @Test
    override fun searchRecipe() = super.searchRecipe()
}
