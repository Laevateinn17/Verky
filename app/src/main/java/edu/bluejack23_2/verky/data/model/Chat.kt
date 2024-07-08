package edu.bluejack23_2.verky.data.model

data class Chat(
    val chatId: String = "",
    val partnerUser: User? = null,
    val message: List<Message> = emptyList()
) {
    constructor() : this("", null, emptyList())
    fun countMessagesWithStatusTrue(): Int {
        return message.count { it!!.status }
    }
}