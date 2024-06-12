package edu.bluejack23_2.verky.ViewModel

import android.view.View
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.verky.Listener.AuthListener
import edu.bluejack23_2.verky.Repository.UserRepository

class LoginViewModel : ViewModel() {

    var email : String? = null
    var password : String? = null

    var authListener: AuthListener? = null;

    fun onLoginButtonClick(view : View){
        val userRepository = UserRepository;
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            authListener?.OnFailure("All fields must be filled")
            return
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email!!).matches()) {
            authListener?.OnFailure("Invalid email format")
        }
        userRepository.login(email!!, password!!) { isSuccess ->
            if (isSuccess) {
                authListener?.OnSuccess()
            } else {
                authListener?.OnFailure("Authentication failed")
            }
        }
    }

}