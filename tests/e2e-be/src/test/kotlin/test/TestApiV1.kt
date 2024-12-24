package olesyaiv.recipebook.e2e.be.test

import olesyaiv.recipebook.e2e.be.fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import olesyaiv.recipebook.api.v1.models.RecipeSearchFilter
import olesyaiv.recipebook.api.v1.models.RecipeUpdateObject
import olesyaiv.recipebook.e2e.be.test.action.v1.*

fun FunSpec.testApiV1(client: Client, prefix: String = "") {
    context("${prefix}v1") {
        test("Create Recipe ok") {
            client.createRecipe()
        }

        test("Read Recipe ok") {
            val created = client.createRecipe()
            client.readRecipe(created.id).asClue {
                it shouldBe created
            }
        }

        test("Update Recipe ok") {
            val created = client.createRecipe()
            val updateRecipe = RecipeUpdateObject(
                id = created.id,
                lock = created.lock,
                name = "Updated Cake",
                description = created.description,
                ingredients = created.ingredients,
            )
            client.updateRecipe(updateRecipe)
        }

        test("Delete Recipe ok") {
            val created = client.createRecipe()
            client.deleteRecipe(created)
        }

        test("Search Recipe ok") {
            val created1 = client.createRecipe(someCreateRecipe.copy(name = "Cake"))
            val created2 = client.createRecipe(someCreateRecipe.copy(name = "Honey Cake"))

            withClue("Search S") {
                val results = client.searchRecipe(search = RecipeSearchFilter(searchString = "C"))
                results shouldHaveSize 2
                results shouldExist { it.name == created1.name }
                results shouldExist { it.name == created2.name }
            }

            withClue("Honey") {
                client.searchRecipe(search = RecipeSearchFilter(searchString = "Honey"))
                    .shouldExistInOrder({ it.name == created2.name })
            }
        }

        test("Cost Recipe ok") {
            val recipe = client.createRecipe(someCreateRecipe)
            withClue("Create ingredients cost for recipe") {
                val res = client.getRecipeCost(recipe.id)
                res.cost shouldBe 30F
            }
        }
    }
}
