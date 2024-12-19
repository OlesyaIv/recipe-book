package models

enum class RecipeIngredient(
    val unit: String,
    val cost: Float) {
    FLOUR("kg", 500F),
    WATER("l", 100F),
    SALT("gram", 1F),
    SUGAR("gram", 2F),
    EGG("unit", 15F),
    BAKING_POWDER("gram", 2F),
    CREAM("gram", 20F);

    companion object {
        fun fromName(name: String): RecipeIngredient? {
            return entries.firstOrNull { it.name == name }
        }
    }
}
