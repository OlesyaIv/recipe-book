package models

import kotlin.jvm.JvmInline

@JvmInline
value class RecipeBookRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = RecipeBookRequestId("")
    }
}
