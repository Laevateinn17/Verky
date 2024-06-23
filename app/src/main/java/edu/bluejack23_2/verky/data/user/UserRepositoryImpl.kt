package edu.bluejack23_2.verky.data.user

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.data.utils.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : UserRepository {
    override suspend fun setLoggedUser(userId: String) {
        val userData = firebaseDatabase.getReference("users").child(userId);
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

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dob: Date? = try {
                    dateFormat.parse(dobString)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                val galleryPicture = snapshot.child("gallery_picture").children.mapNotNull { it.getValue(String::class.java) }

                val user = User(userId, name, email, dob!!, gender, religion, activities, incognitoMode, profilePicture, galleryPicture)
                LoggedUser.getInstance().setUser(user)
                Resource.Success(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Resource.Failure(error.toException());
            }
        })
    }
}