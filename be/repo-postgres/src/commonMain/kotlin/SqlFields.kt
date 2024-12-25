package olesyaiv.recipebook.be.repo.postgresql

object SqlFields {
    const val ID = "id"
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val INGREDIENTS = "ingredients"
    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"
    const val OWNER_ID = "owner_id"

    const val FILTER_NAME = TITLE
    const val FILTER_OWNER_ID = OWNER_ID

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, TITLE, DESCRIPTION, INGREDIENTS, LOCK, OWNER_ID,
    )
}
