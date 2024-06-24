package edu.bluejack23_2.verky.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.auth.AuthRepository
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

//    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
//    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signUpFlow

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init{
        if(currentUser != null){
            _loginFlow.value = Resource.Success(authRepository.currentUser!!)
            Log.e("curreuser", currentUser!!.uid)
            fetchUserLogged(authRepository.currentUser!!.uid)
        }
//        Log.e("user", authRepository.currentUser!!.uid)
//        Log.e("user_name", LoggedUser.getInstance().getUser()!!.name)
//        _loginFlow.value = null
    }

    fun fetchUserLogged(userId: String) {
        viewModelScope.launch {
            try {
                userRepository.setLoggedUser(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun login(email: String, password : String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = authRepository.login(email, password)
        _loginFlow.value = result
    }

//    fun signUp(name : String, email: String, password : String) = viewModelScope.launch {
//        _loginFlow.value = Resource.Loading
//        val result = repository.signUp(name, email, password)
//        _loginFlow.value = result
//    }

    fun saveCredentials(email: String, password: String, userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putString("userID", userId)
        editor.apply()
    }

    fun getEmail(): String? {
        return sharedPreferences.getString("email", null)
    }

    fun getPassword(): String? {
        return sharedPreferences.getString("password", null)
    }

    suspend fun loginWithSavedCredentials() {
        val email = getEmail()
        val password = getPassword()
        if (email != null && password != null) {
            authRepository.login(email, password)
        }
    }

    private fun clearCredentialsFromPreferences() {
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.apply()
    }

    fun logOut(){
        authRepository.logOut();
        _loginFlow.value = null
//        _signUpFlow.value = null
    }

}