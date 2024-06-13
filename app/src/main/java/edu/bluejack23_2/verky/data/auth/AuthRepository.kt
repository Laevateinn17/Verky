package edu.bluejack23_2.verky.data.auth

import com.google.firebase.auth.FirebaseUser
import edu.bluejack23_2.verky.data.Resource

interface AuthRepository {

    val currentUser : FirebaseUser?
    suspend fun login(email : String,  password : String): Resource<FirebaseUser>
//    suspend fun signUp(name : String, email : String, password : String): Resource<FirebaseUser>
    fun logOut()
}