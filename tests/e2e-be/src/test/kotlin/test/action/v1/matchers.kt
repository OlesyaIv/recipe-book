package olesyaiv.recipebook.e2e.be.test.action.v1

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.and
import olesyaiv.recipebook.api.v1.models.*


fun haveResult(result: ResponseResult) = Matcher<IResponse> {
    MatcherResult(
        it.result == result,
        { "actual result ${it.result} but we expected $result" },
        { "result should not be $result" }
    )
}

val haveNoErrors = Matcher<IResponse> {
    MatcherResult(
        it.errors.isNullOrEmpty(),
        { "actual errors ${it.errors} but we expected no errors" },
        { "errors should not be empty" }
    )
}

val haveSuccessResult = haveResult(ResponseResult.SUCCESS) and haveNoErrors

val IResponse.recipe: RecipeResponseObject?
    get() = when (this) {
        is RecipeCreateResponse -> recipe
        is RecipeReadResponse -> recipe
        is RecipeUpdateResponse -> recipe
        is RecipeDeleteResponse -> recipe
        is RecipeCostResponse -> recipe
        else -> throw IllegalArgumentException("Invalid response type: ${this::class}")
    }
