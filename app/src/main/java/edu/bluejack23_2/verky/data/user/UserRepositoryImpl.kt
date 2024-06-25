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
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : UserRepository {

    private val userRef: DatabaseReference by lazy {firebaseDatabase.getReference("users")}
    private val religionRef: DatabaseReference by lazy { firebaseDatabase.getReference("religion") }
    private val interestRef: DatabaseReference by lazy { firebaseDatabase.getReference("interest") }

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


}