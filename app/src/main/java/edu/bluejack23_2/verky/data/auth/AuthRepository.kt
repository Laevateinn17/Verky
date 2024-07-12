package edu.bluejack23_2.verky.data.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.model.User

interface AuthRepository {

    val currentUser : FirebaseUser?
    suspend fun login(email : String,  password : String): Resource<FirebaseUser>
    suspend fun getCurrentUserToken(): String?


    fun uploadImagesToFirebase(
        images: List<Uri>,
        onSuccess: (List<String>) -> Unit,
        onFailure: (String) -> Unit
    )
    suspend fun signUp(name : String, email : String, password : String): Resource<FirebaseUser>

    fun logOut()
}