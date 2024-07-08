package edu.bluejack23_2.verky.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.bluejack23_2.verky.data.chat.ChatRepository
import edu.bluejack23_2.verky.data.model.Chat
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.Message
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chatList = MutableLiveData<List<Chat>>()
    val chatList: LiveData<List<Chat>>
        get() = _chatList

    init {
        LoggedUser.getInstance().getUser()?.id?.let { fetchChatList(it) }
    }

    private fun fetchChatList(userID: String){
        chatRepository.getChatList(userID) { chats ->
            _chatList.postValue(chats)
        }
    }

    fun fetchMessage(chatID : String)  : LiveData<List<Message>>{
        return chatRepository.fetchMessage(chatID)
    }

    fun sendMessage(chatID : String, message: Message){
        chatRepository.sendMessage(message, chatID)
    }
}