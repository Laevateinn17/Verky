package edu.bluejack23_2.verky.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

//    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
//    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signUpFlow

    private val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init{
        if(currentUser != null){
            _loginFlow.value = Resource.Success(authRepository.currentUser!!)
            fetchUserLogged(authRepository.currentUser!!.uid)
        }
//        _loginFlow.value = null
    }

    private fun fetchUserLogged(userId: String) {
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

    fun logOut(){
        authRepository.logOut();
        _loginFlow.value = null
//        _signUpFlow.value = null
    }

}