package edu.bluejack23_2.verky.data.chat

import com.google.firebase.database.*
import edu.bluejack23_2.verky.data.model.Chat
import edu.bluejack23_2.verky.data.model.Message
import edu.bluejack23_2.verky.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

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
        val chatList = mutableListOf<Chat>()

        val query = chatsRef.orderByChild("user_list/$userID").equalTo(true)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { chatSnapshot ->
                    val chatId = chatSnapshot.key
                    val messages = mutableListOf<Message>()

                    chatSnapshot.child("messages").children.forEach { messageSnapshot ->
                        val messageId = messageSnapshot.key
                        val content = messageSnapshot.child("content").getValue(String::class.java)
                        val status = messageSnapshot.child("status").getValue(Boolean::class.java)
                        val timeStamp = messageSnapshot.child("timestamp").getValue(String::class.java)
                        val userId = messageSnapshot.child("userID").getValue(String::class.java)

                        val message = Message(messageId!!, content!!, status!!, timeStamp!!, userId!!)
                        messages.add(message)
                    }

                    val users = mutableListOf<String>()
                    chatSnapshot.child("user_list").children.forEach { userSnapshot ->
                        val userId = userSnapshot.key
                        userId?.let { users.add(it) }
                    }

                    val partnerID = users.firstOrNull { it != userID }

                    partnerID?.let { partnerId ->
                        usersRef.child(partnerId).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                val partnerUser = userSnapshot.getValue(User::class.java)
                                partnerUser?.let {
                                    val chat = Chat(chatId!!, partnerUser, messages)
                                    chatList.add(chat)

                                    if (chatList.size == dataSnapshot.childrenCount.toInt()) {
                                        callback(chatList)
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle onCancelled
                            }
                        })
                    }
                }

                if (dataSnapshot.childrenCount == 0L) {
                    callback(chatList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
