package edu.bluejack23_2.verky.data.model

data class Message(
    val messageId: String = "",
    val content: String = "",
    val status: Boolean = false,
    val timestamp: Long? = null,
    val userID: String = "",
    var user: User?
) {
    constructor() : this("", "", false, null, "", null)
}