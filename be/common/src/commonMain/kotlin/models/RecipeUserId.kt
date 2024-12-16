package models

import kotlin.jvm.JvmInline

@JvmInline
value class RecipeUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = RecipeUserId("")
    }
}
