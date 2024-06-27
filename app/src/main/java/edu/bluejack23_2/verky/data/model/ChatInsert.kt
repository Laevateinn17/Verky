package edu.bluejack23_2.verky.data.model

data class ChatInsert(
    val chatId: String = "",
    val messages: List<Message> = emptyList(),
    val user_list : List<String> = emptyList()
) {
    constructor() : this("", emptyList(), emptyList())
    fun countMessagesWithStatusTrue(): Int {
        return messages.count { it!!.status }
    }
}