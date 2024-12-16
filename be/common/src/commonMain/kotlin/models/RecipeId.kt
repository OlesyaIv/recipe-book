package models

import kotlin.jvm.JvmInline

@JvmInline
value class RecipeId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = RecipeId("")
    }
}
