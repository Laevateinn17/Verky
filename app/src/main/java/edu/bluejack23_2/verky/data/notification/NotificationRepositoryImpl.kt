package edu.bluejack23_2.verky.data.notification

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack23_2.verky.data.model.Chat
import edu.bluejack23_2.verky.data.model.ChatInsert
import edu.bluejack23_2.verky.data.model.Message
import edu.bluejack23_2.verky.data.model.Notification
import edu.bluejack23_2.verky.data.model.User
import java.util.UUID
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : NotificationRepository{
    private val notificationRef: DatabaseReference by lazy { firebaseDatabase.getReference("notification") }
    private val rejectedUsersRef: DatabaseReference by lazy { firebaseDatabase.getReference("rejectedUsers") }
    private val usersRef: DatabaseReference by lazy { firebaseDatabase.getReference("users") }

    override fun getNotificationList(userID: String, callback: (List<Notification>) -> Unit) {
        val notifications = mutableListOf<Notification>()

        notificationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notifications.clear()
                for (dataSnapshot in snapshot.children) {
                    val notification = dataSnapshot.getValue(Notification::class.java)
                    if (notification != null && notification.to == userID) {
                        notification.notificationID = dataSnapshot.key ?: ""
                        val fromUserId = notification.from

                        if (fromUserId != null) {
                            usersRef.child(fromUserId).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(userSnapshot: DataSnapshot) {
                                    val fromUser = userSnapshot.getValue(User::class.java)
                                    notification.fromUser = fromUser
                                    notifications.add(notification)
                                    callback(notifications)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle possible errors
                                }
                            })
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    override fun acceptNotification(notif: Notification) {

        val userList = listOf(notif.from, notif.to)

        val chat = ChatInsert(
            chatId = UUID.randomUUID().toString(),
            messages = emptyList(),
            user_list = userList
        )

        val chatRef = firebaseDatabase.getReference("chats")
        chatRef.child(chat.chatId).setValue(chat)
            .addOnSuccessListener {
                Log.d("NotificationRepository", "Chat successfully created")
            }
            .addOnFailureListener { e ->
                Log.e("NotificationRepository", "Error creating chat", e)
            }

        rejectNotification(notif)
    }

    override fun rejectNotification(notif: Notification) {
        val notif2 = Notification(from = notif.to, to = notif.from);
        val rejectUserRef = rejectedUsersRef.push()
        val rejectUserRef2 = rejectedUsersRef.push()

        rejectUserRef.setValue(notif)
            .addOnSuccessListener {
                Log.d("NotificationRepository", "Notification successfully rejected and sent to 1")
            }
            .addOnFailureListener { e ->
                Log.e("NotificationRepository", "Error rejecting 1", e)
            }

        rejectUserRef2.setValue(notif2)
            .addOnSuccessListener {
                Log.d("NotificationRepository", "Notification successfully rejected and sent to sender")
            }
            .addOnFailureListener { e ->
                Log.e("NotificationRepository", "Error rejecting notification", e)
            }

        val notificationId = notif.notificationID
        notificationRef.child(notificationId).removeValue()
            .addOnSuccessListener {
                Log.d("NotificationRepository", "Notification successfully removed")
            }
            .addOnFailureListener { e ->
                Log.e("NotificationRepository", "Error removing notification", e)
            }
    }

    override fun addNotification(notif: Notification) {
        val notifRef = notificationRef.push()
        notifRef.setValue(notif)
    }

    override fun declineNotification(notif: Notification) {
        val rejectUserRef = rejectedUsersRef.push()
        rejectUserRef.setValue(notif)
    }

}