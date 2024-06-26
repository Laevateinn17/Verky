package edu.bluejack23_2.verky.data.auth

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.user.UserRepository
import edu.bluejack23_2.verky.data.utils.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val userRepository: UserRepository,
    private val firebaseStorage: FirebaseStorage,
): AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try{
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            userRepository.setLoggedUser(result.user!!.uid)
            Resource.Success(result.user!!)
        }catch (e: Exception){
            e.printStackTrace();
            Resource.Failure(e)
        }
    }

    override suspend fun signUp(name : String, email: String, password: String): Resource<FirebaseUser> {
        return try{
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            Resource.Success(result.user!!)
        }catch (e: Exception){
            e.printStackTrace();
            Resource.Failure(e)
        }
    }

    override fun logOut() {
        firebaseAuth.signOut();
    }

    override suspend fun getCurrentUserToken(): String? {
        currentUser?.getIdToken(true)?.await()?.token?.let { Log.e("current_user", it) }
        return currentUser?.getIdToken(true)?.await()?.token
    }

    override fun uploadImagesToFirebase(
        images: List<Uri>,
        onSuccess: (List<String>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val uploadedImageUrls = mutableListOf<String>()
        var uploadCount = 0

        for (uri in images) {
            val imageRef = firebaseStorage.reference.child("images/${uri.lastPathSegment}")

            imageRef.putFile(uri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        uploadedImageUrls.add(downloadUri.toString())
                        uploadCount++
                        if (uploadCount == images.size) {
                            onSuccess(uploadedImageUrls)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Unknown error occurred")
                    return@addOnFailureListener
                }
        }
    }

}