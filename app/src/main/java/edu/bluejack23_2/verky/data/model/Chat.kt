package edu.bluejack23_2.verky.data.model

data class Chat (
    val chatId : String,
    val partnerUser : User?,
    val message : List<Message>
){
    fun countMessagesWithStatusTrue(): Int {
        return message.count { it.status }
    }
}