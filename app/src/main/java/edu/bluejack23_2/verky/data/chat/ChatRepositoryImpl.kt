package edu.bluejack23_2.verky.data.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.data.model.Chat
import edu.bluejack23_2.verky.data.model.Message
import edu.bluejack23_2.verky.data.model.User
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ChatRepository {

    private val chatsRef: DatabaseReference by lazy { firebaseDatabase.getReference("chats") }
    private val usersRef: DatabaseReference by lazy { firebaseDatabase.getReference("users") }

    override fun sendMessage(message: Message, chatId : String) {
        val messageRef = chatsRef.child(chatId).child("message").push()
        messageRef.setValue(message)
    }

    override fun fetchMessage(chatId: String): LiveData<List<Message>> {
        val messagesLiveData = MutableLiveData<List<Message>>()
        val query = chatsRef.child(chatId).child("message").orderByChild("timestamp")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = mutableListOf<Message>()
                for (data in snapshot.children) {
                    val message = data.getValue(Message::class.java)
                    if (message != null) {
                        fetchUserForMessage(message) { user ->
                            message.user = user
                            messageList.add(message)
                            messagesLiveData.value = messageList
                        }
//                        messageList.add(message)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
        return messagesLiveData
    }

    private fun fetchUserForMessage(message: Message, callback: (User?) -> Unit) {
        val userId = message.userID
        usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                callback(user)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    override fun getChatList(userID: String, callback: (List<Chat>) -> Unit) {
        val tasks = mutableListOf<Task<Chat?>>()
        chatsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tasks.clear()
                for (chatSnapshot in snapshot.children) {
                    val userListSnapshot = chatSnapshot.child("user_list")
                    val userList =
                        userListSnapshot.children.mapNotNull { it.getValue(String::class.java) }

                    if (userList.contains(userID)) {
                        val chatId = chatSnapshot.key ?: continue
                        val partnerUserId = userList.find { it != userID }

                        partnerUserId?.let {
                            val userTask = usersRef.child(it).get().continueWith { task ->
                                val partnerUserSnapshot = task.result
                                val partnerUser = partnerUserSnapshot.getValue(User::class.java)

                                val messagesSnapshot = chatSnapshot.child("message")
                                val messages = messagesSnapshot.children.mapNotNull { msgSnap ->
                                    msgSnap.getValue(Message::class.java)
                                }

                                if (partnerUser != null) {
                                    partnerUser.id = partnerUserId
                                    Chat(chatId, partnerUser, messages)
                                } else {
                                    null
                                }
                            }
                            tasks.add(userTask)
                        }
                    }
                }

                Tasks.whenAll(tasks).addOnCompleteListener {
                    val chats = tasks.mapNotNull { it.result }
                    callback(chats)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}
