import ws.IRecipeBookWsSessionRepo

data class RecipeBookCorSettings(
    val wsSessions: IRecipeBookWsSessionRepo = IRecipeBookWsSessionRepo.NONE
) {
    companion object {
        val NONE = RecipeBookCorSettings()
    }
}
