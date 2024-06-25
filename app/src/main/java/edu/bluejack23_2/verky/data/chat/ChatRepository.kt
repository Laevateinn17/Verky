package edu.bluejack23_2.verky.data.chat

import edu.bluejack23_2.verky.data.model.Chat
import edu.bluejack23_2.verky.data.model.Message

interface ChatRepository {
    fun sendMessage(message: Message, chat: Chat)
    fun getChatList(userID: String, callback:(List<Chat>) -> Unit)
}