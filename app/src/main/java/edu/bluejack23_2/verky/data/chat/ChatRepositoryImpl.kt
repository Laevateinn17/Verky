package edu.bluejack23_2.verky.data.chat

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import edu.bluejack23_2.verky.data.model.Chat
import edu.bluejack23_2.verky.data.model.Message
import edu.bluejack23_2.verky.data.model.User
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ChatRepository {

    private val chatsRef: DatabaseReference by lazy { firebaseDatabase.getReference("chats") }
    private val usersRef: DatabaseReference by lazy { firebaseDatabase.getReference("users") }

    override fun sendMessage(message: Message, chat: Chat) {
        val messageRef = chatsRef.child(chat.chatId).child("messages").push()
        messageRef.setValue(message)
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

                                val messagesSnapshot = chatSnapshot.child("messages")
                                val messages = messagesSnapshot.children.mapNotNull { msgSnap ->
                                    msgSnap.getValue(Message::class.java)
                                }

                                if (partnerUser != null) {
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
