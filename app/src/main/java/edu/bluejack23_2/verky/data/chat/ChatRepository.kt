package edu.bluejack23_2.verky.data.chat

import androidx.lifecycle.LiveData
import edu.bluejack23_2.verky.data.model.Chat
import edu.bluejack23_2.verky.data.model.Message

interface ChatRepository {
    fun sendMessage(message: Message, chatId: String)
    fun fetchMessage(chatId : String): LiveData<List<Message>>
    fun getChatList(userID: String, callback:(List<Chat>) -> Unit)
}