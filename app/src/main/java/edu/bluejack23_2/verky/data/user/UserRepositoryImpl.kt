package edu.bluejack23_2.verky.data.user

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.User
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : UserRepository {

    private val userRef: DatabaseReference by lazy {firebaseDatabase.getReference("users")}
    private val religionRef: DatabaseReference by lazy { firebaseDatabase.getReference("religion") }
    private val interestRef: DatabaseReference by lazy { firebaseDatabase.getReference("interest") }
    private val rejectedUsersRef: DatabaseReference by lazy { firebaseDatabase.getReference("rejectedUsers")}

    override suspend fun setLoggedUser(userId: String) {
        val userData = userRef.child(userId);
        userData.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java) ?: ""
                val email = snapshot.child("email").getValue(String::class.java) ?: ""
                val dobString = snapshot.child("dob").getValue(String::class.java) ?: ""
                val gender = snapshot.child("gender").getValue(String::class.java) ?: ""
                val religion = snapshot.child("religion").getValue(String::class.java) ?: ""
                val activities = snapshot.child("activities").children.mapNotNull { it.getValue(String::class.java) }
                val incognitoMode = snapshot.child("incognito_mode").getValue(Boolean::class.java) ?: false
                val profilePicture = snapshot.child("profile_picture").getValue(String::class.java) ?: ""
2
                val galleryPicture = snapshot.child("gallery_picture").children.mapNotNull { it.getValue(String::class.java) }

                val user = User(userId, name, email, dobString, gender, religion, activities, incognitoMode, profilePicture, galleryPicture)
                LoggedUser.getInstance().setUser(user)
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

    override suspend fun  getPotentialMatch() : User {
        val currentUserId = LoggedUser.getInstance().getUser()?.id ?: throw Exception("User not logged in")
        val rejectedUsers = getRejectedUsers(currentUserId)

        return suspendCancellableCoroutine { continuation ->
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        val potentialMatch = childSnapshot.getValue(User::class.java)
                        if (potentialMatch != null && potentialMatch.id !in rejectedUsers) {
                            continuation.resume(potentialMatch)
                            return
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

    private suspend fun getRejectedUsers(currentUserId: String): List<String> = suspendCancellableCoroutine { continuation ->
        rejectedUsersRef.child(currentUserId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rejectedUsers = snapshot.children.mapNotNull { it.getValue(String::class.java) }
                continuation.resume(rejectedUsers)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
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




}