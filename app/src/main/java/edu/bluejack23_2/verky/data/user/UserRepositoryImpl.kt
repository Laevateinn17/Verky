package edu.bluejack23_2.verky.data.user

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.model.Chat
import edu.bluejack23_2.verky.data.model.LogUser
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.Notification
import edu.bluejack23_2.verky.data.model.User
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : UserRepository {

    private val userRef: DatabaseReference by lazy {firebaseDatabase.getReference("users")}
    private val religionRef: DatabaseReference by lazy { firebaseDatabase.getReference("religion") }
    private val interestRef: DatabaseReference by lazy { firebaseDatabase.getReference("interest") }
    private val notificationRef: DatabaseReference by lazy { firebaseDatabase.getReference("notification")}
    private val chatRef: DatabaseReference by lazy { firebaseDatabase.getReference("chats")}
    private val rejectedUsersRef: DatabaseReference by lazy { firebaseDatabase.getReference("rejectedUsers") }

    override suspend fun setLoggedUser(userId: String) {
        val userData = userRef.child(userId);
        userData.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java) ?: ""
                val email = snapshot.child("email").getValue(String::class.java) ?: ""
                val dobString = snapshot.child("dob").getValue(String::class.java) ?: ""
                val gender = snapshot.child("gender").getValue(String::class.java) ?: ""
                val religion = snapshot.child("religion").getValue(String::class.java) ?: ""
                val interests = snapshot.child("interest").children.mapNotNull { it.getValue(String::class.java) }
                val incognitoMode = snapshot.child("incognito_mode").getValue(Boolean::class.java) ?: false
                val profilePicture = snapshot.child("profile_picture").getValue(String::class.java) ?: ""
                val height = snapshot.child("height").getValue(Int::class.java) ?: 0
                val galleryPicture = snapshot.child("gallery_picture").children.mapNotNull { it.getValue(String::class.java) }

                val user = User(userId, name, email, dobString, gender, religion, interests, incognitoMode, profilePicture, height, galleryPicture)
                LoggedUser.getInstance().setUser(user)
                LogUser.setUser(user)
                Resource.Success(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Resource.Failure(error.toException());
            }
        })
    }

    override suspend fun getReligionData(): List<String> = suspendCancellableCoroutine { continuation ->
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.mapNotNull { it.getValue(String::class.java) }
                continuation.resume(data)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        }

        religionRef.addListenerForSingleValueEvent(listener)

        continuation.invokeOnCancellation {
            religionRef.removeEventListener(listener)
        }
    }

    override suspend fun getInterestData(): List<String> = suspendCancellableCoroutine { continuation ->
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.mapNotNull { it.getValue(String::class.java) }
                continuation.resume(data)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        }

        interestRef.addListenerForSingleValueEvent(listener)

        continuation.invokeOnCancellation {
            interestRef.removeEventListener(listener)
        }
    }

    override suspend fun isEmailAlreadyUsed(email: String): Boolean {
        val snapshot = userRef.orderByChild("email").equalTo(email).get().await()
        return snapshot.exists()
    }

    override suspend fun getUser(userID: String): User {
        return suspendCancellableCoroutine { continuation ->
            userRef.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        continuation.resume(user)
                    } else {
                        continuation.resumeWithException(Exception("User not found"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }

    override suspend fun getPotentialMatch() : User {
        val currentUserId = LoggedUser.getInstance().getUser()?.id ?: throw Exception("User not logged in")
        val rejectedUsers = getRejectedUsers(currentUserId)
        val notificationUsers = getNotificationUsers(currentUserId)
        val chatUsers = getChatUsers(currentUserId)

        return suspendCancellableCoroutine { continuation ->
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentUserGender = LogUser.getUser().value?.gender
                    for (childSnapshot in snapshot.children) {
                        val potentialMatch = childSnapshot.getValue(User::class.java)
                        if (potentialMatch != null) {
                            potentialMatch.id = childSnapshot.key

                            if (currentUserGender == "Male" && potentialMatch.gender == "Female" ||
                                currentUserGender == "Female" && potentialMatch.gender == "Male") {
                                if (potentialMatch.id !in rejectedUsers &&
                                    potentialMatch.id !in notificationUsers &&
                                    potentialMatch.id !in chatUsers &&
                                    potentialMatch.id != currentUserId) {
                                    continuation.resume(potentialMatch)
                                    return
                                }
                            }
                        }
                    }
                    continuation.resumeWithException(Exception("No potential match"))
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }

    private suspend fun getRejectedUsers(currentUserId: String): List<String> {
        val toUserIds = mutableSetOf<String>()
        val snapshot = rejectedUsersRef.get().await()

        snapshot.children.forEach { notificationSnapshot ->
            val notification = notificationSnapshot.getValue(Notification::class.java)
            if (notification != null) {
                if(notification.from.equals(currentUserId)){
                    toUserIds.add(notification.to)
                }
            }
        }

        return toUserIds.toList()
    }

    private suspend fun getNotificationUsers(currentUserId : String): List<String> {
        val toUserIds = mutableSetOf<String>()
        val snapshot = notificationRef.get().await()

        snapshot.children.forEach { notificationSnapshot ->
            val notification = notificationSnapshot.getValue(Notification::class.java)
            if (notification != null) {
                if(notification.from.equals(currentUserId)){
                    toUserIds.add(notification.to)
                }
                else if(notification.to.equals(currentUserId)){
                    toUserIds.add(notification.from)
                }
            }
        }

        return toUserIds.toList()
    }

    private suspend fun getChatUsers(currentUserId: String): List<String> {
        val toUserIds = mutableSetOf<String>()
        val snapshot = chatRef.get().await()

        snapshot.children.forEach { chatSnapshot ->
            val userListSnapshot = chatSnapshot.child("user_list")
            val userList =
                userListSnapshot.children.mapNotNull { it.getValue(String::class.java) }

            if(userList.contains(currentUserId)){
                for (user in userList){
                    if(user != currentUserId){
                        toUserIds.add(user)
                    }
                }
            }
        }

        return toUserIds.toList()
    }

    override suspend fun addUser(user: User, userID : String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (userID != null) {
            userRef.child(userID).setValue(user)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Unknown error occurred")
                }
        } else {
            onFailure("Failed to generate user ID")
        }
    }

    override suspend fun updateUser(user: User) {
        val userId = user.id ?: return

        val usersRef = userRef.child(userId)

        try {
            val updateMap = mutableMapOf<String, Any>()
            updateMap["name"] = user.name
            updateMap["email"] = user.email
            updateMap["dob"] = user.dob
            updateMap["gender"] = user.gender
            updateMap["height"] = user.height
            updateMap["interest"] = user.interest

            usersRef.updateChildren(updateMap).await()
        } catch (e: Exception) {
            // Handle error
            e.printStackTrace()
            // Consider providing a more specific error message based on the exception
        }
    }

}