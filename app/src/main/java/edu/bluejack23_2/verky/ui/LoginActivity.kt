package edu.bluejack23_2.verky.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.databinding.ActivityLoginBinding
import edu.bluejack23_2.verky.util.toast

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(){

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(validate()){
            binding.loginButton.setOnClickListener {
                viewModel.login(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
        }

        setContent{
            LoginObserver(viewModel)
        }
    }

    @Composable
    fun LoginObserver(viewModel: AuthViewModel) {
        val loginState = viewModel.loginFlow.collectAsState()

        loginState?.value?.let{
            when(it){
                is Resource.Failure -> {
                    val context = LocalContext.current
                    it.exception.message?.let { it1 -> toast(it1) }
                }
                Resource.Loading -> {
                    //Loading State
                }
                is Resource.Success<*> -> {
                    //navController -> perlu di research
                    //navigate to Home
                }
            }
        }
    }

    fun validate() : Boolean{
        var isValid = true;
        val email = binding.emailEditText.text.toString()
        val pass = binding.passwordEditText.text.toString()
        if(email.isNullOrEmpty() || pass.isNullOrEmpty()){
            toast("All input must be filled!")
            isValid = false;
        }
        return isValid;
    }

}