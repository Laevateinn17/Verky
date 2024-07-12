package edu.bluejack23_2.verky.data.model

data class Chat(
    val chatId: String = "",
    val partnerUser: User? = null,
    val message: List<Message> = emptyList(),
    val user_list: List<String>? = null,
) {
    constructor() : this("", null, emptyList(), null)
    fun countMessagesWithStatusTrue(): Int {
        return message.count { it!!.status }
    }
}