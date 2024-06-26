package edu.bluejack23_2.verky.data.model

data class Notification(
    val chatId: String = "",
    var fromUser: User? = null,
) {
    constructor() : this("", null)
}