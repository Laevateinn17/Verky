package edu.bluejack23_2.verky.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.utils.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth
): AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try{
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        }catch (e: Exception){
            e.printStackTrace();
            Resource.Failure(e)
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