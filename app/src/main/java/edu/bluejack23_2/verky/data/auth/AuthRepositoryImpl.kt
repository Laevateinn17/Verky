package edu.bluejack23_2.verky.data.auth

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.model.LoginUser
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.data.utils.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
): AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try{
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            setUserData(result.user!!.uid)
            Resource.Success(result.user!!)
        }catch (e: Exception){
            e.printStackTrace();
            Resource.Failure(e)
        }
    }

    override suspend fun setUserData(userId: String): Resource<User> {
        return try {
            val userData = firebaseDatabase.getReference("users").child(userId);
            val dataSnapshot = userData.get().await();

            if(dataSnapshot.exists()){
                val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
                val email = dataSnapshot.child("email").getValue(String::class.java) ?: ""
                val dob_string = dataSnapshot.child("dob").getValue(String::class.java) ?: ""
                val gender = dataSnapshot.child("gender").getValue(String::class.java) ?: ""
                val religion = dataSnapshot.child("religion").getValue(String::class.java) ?: ""
                val activities = dataSnapshot.child("activities").children.mapNotNull { it.getValue(String::class.java) }
                val incognitoMode = dataSnapshot.child("incognito_mode").getValue(Boolean::class.java) ?: false
                val profilePicture = dataSnapshot.child("profile_picture").getValue(String::class.java) ?: ""

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dob: Date? = try {
                    dateFormat.parse(dob_string)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                val user = User(name, email, dob!!, gender, religion, activities, incognitoMode, profilePicture)
                LoginUser.getInstance().setUser(user)

                val user_logged : User? = LoginUser.getInstance().getUser();
                if(user_logged != null){
                    Log.e("test", user_logged.email)
                }
                Resource.Success(user)
            }
            else {
                Resource.Failure(Exception("User data not found"))
            }
        }
        catch (e : Exception){
            e.printStackTrace();
            Resource.Failure(e);
        }
    }

//    override suspend fun signup(email: String, password: String): Resource<FirebaseUser> {
//        return try{
//            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
//            result?.user?.updateProfile(
//                UserProfileChangeRequest.Builder().setDisplayName(name).build()
//            )?.await()
//            Resource.Success(result.user!!)
//        }catch (e: Exception){
//            e.printStackTrace();
//            Resource.Failure(e)
//        }
//    }

    override fun logOut() {
        firebaseAuth.signOut();
    }

}