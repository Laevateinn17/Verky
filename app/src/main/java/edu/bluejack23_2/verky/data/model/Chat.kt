package edu.bluejack23_2.verky.data.model

data class Chat(
    val chatId: String = "",
    val partnerUser: User? = null,
    val messages: List<Message> = emptyList()
) {
    constructor() : this("", null, emptyList())
    fun countMessagesWithStatusTrue(): Int {
        return messages.count { it!!.status }
    }
}