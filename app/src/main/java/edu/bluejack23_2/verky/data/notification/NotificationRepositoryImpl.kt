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
import edu.bluejack23_2.verky.data.model.Message
import edu.bluejack23_2.verky.data.model.Notification
import edu.bluejack23_2.verky.data.model.User
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : NotificationRepository{
    private val notificationRef: DatabaseReference by lazy { firebaseDatabase.getReference("notification") }
    private val usersRef: DatabaseReference by lazy { firebaseDatabase.getReference("users") }

    override fun getNotificationList(userID: String, callback: (List<Notification>) -> Unit) {
        val notifications = mutableListOf<Notification>()

        notificationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notifications.clear()
                for (dataSnapshot in snapshot.children) {
                    val notification = dataSnapshot.getValue(Notification::class.java)
                    if (notification != null && notification.to == userID) {
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
}