package edu.bluejack23_2.verky.data.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.data.user.UserRepository
import edu.bluejack23_2.verky.data.user.UserRepositoryImpl
import edu.bluejack23_2.verky.data.utils.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val userRepository: UserRepository
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

    override suspend fun getCurrentUserToken(): String? {
        currentUser?.getIdToken(true)?.await()?.token?.let { Log.e("current_user", it) }
        return currentUser?.getIdToken(true)?.await()?.token
    }

}