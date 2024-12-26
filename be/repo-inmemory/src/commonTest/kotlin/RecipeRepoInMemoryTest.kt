class RecipeRepoInMemoryCreateTest : RepoRecipeCreateTest() {
    override val repo = RecipeRepoInitialized(
        RecipeRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class RecipeRepoInMemoryDeleteTest : RepoRecipeDeleteTest() {
    override val repo = RecipeRepoInitialized(
        RecipeRepoInMemory(),
        initObjects = initObjects,
    )
}

class RecipeRepoInMemoryReadTest : RepoRecipeReadTest() {
    override val repo = RecipeRepoInitialized(
        RecipeRepoInMemory(),
        initObjects = initObjects,
    )
}

class RecipeRepoInMemorySearchTest : RepoRecipeSearchTest() {
    override val repo = RecipeRepoInitialized(
        RecipeRepoInMemory(),
        initObjects = initObjects,
    )
}

class RecipeRepoInMemoryUpdateTest : RepoRecipeUpdateTest() {
    override val repo = RecipeRepoInitialized(
        RecipeRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
