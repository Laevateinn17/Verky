package edu.bluejack23_2.verky.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.databinding.ActivityLoginBinding
import edu.bluejack23_2.verky.ui.DashboardActivity
import edu.bluejack23_2.verky.util.toast
import kotlinx.coroutines.launch

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

        binding.loginButton.setOnClickListener{
            if(validate()){
                viewModel.login(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
        }

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        observeViewModel();
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.loginFlow.collect { state ->
                when (state) {
                    is Resource.Failure -> {
                        state.exception.message?.let { toast(it) }
                    }

                    Resource.Loading -> {
                        // Show loading state
                    }

                    is Resource.Success<*> -> {
                        navigateToHome()
                    }

                    else -> {

                    }
                }
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
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