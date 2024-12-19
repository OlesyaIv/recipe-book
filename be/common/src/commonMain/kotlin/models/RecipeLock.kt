package models

import kotlin.jvm.JvmInline

@JvmInline
value class RecipeLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = RecipeLock("")
    }
}
