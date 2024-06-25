package edu.bluejack23_2.verky.data.model

data class Message(
    val messageId: String = "",
    val content: String = "",
    val status: Boolean = false,
    val timestamp: String = "",
    val userID: String = ""
) {
    constructor() : this("", "", false, "", "")
}