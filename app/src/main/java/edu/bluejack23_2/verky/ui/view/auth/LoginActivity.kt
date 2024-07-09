package edu.bluejack23_2.verky.ui.view.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.databinding.ActivityLoginBinding
import edu.bluejack23_2.verky.ui.view.dashboard.DashboardActivity
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel
import edu.bluejack23_2.verky.util.toast
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(){

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }
    private lateinit var binding : ActivityLoginBinding
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var executor: Executor
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executor = ContextCompat.getMainExecutor(this)
        setupBiometricPrompt()

        binding.loginButton.setOnClickListener{
            if(validate()){
                Log.d("validate", "validate login")
                viewModel.login(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
        }

        sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.biometricLogin.setOnClickListener {
            authenticateWithBiometrics()
        }

        observeViewModel();
    }

    private fun authenticateWithBiometrics() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> biometricPrompt.authenticate(promptInfo)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> toast("No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> toast("Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> toast("The user hasn't associated any biometric credentials with their account.")
        }
    }

    private fun setupBiometricPrompt() {
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    toast("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    toast("Authentication succeeded!")
                    lifecycleScope.launch {
                        viewModel.loginWithSavedCredentials()
                    }
                    if(LoggedUser.getInstance().getUser() != null){
                        navigateToHome()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    toast("Authentication failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for Verky")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
    }



    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.loginFlow.collect { state ->
                when (state) {
                    is Resource.Failure -> {
                        state.exception.message?.let { toast(it) }
                    }

                    is Resource.Loading -> {
                        // Show loading state
                    }

                    is Resource.Success<*> -> {
                        Log.e("user", "user login success")
                        val userID = sharedPreferences.getString("userID", null)
                        val biometricManager = BiometricManager.from(this@LoginActivity)
                        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                            BiometricManager.BIOMETRIC_SUCCESS -> {
                                if (userID.isNullOrEmpty() || userID.toString() != viewModel.currentUser?.uid ) {
                                    showBiometricLoginConfirmationDialog()
                                }
                                else {
                                    navigateToHome()
                                }
                            }
                            else -> {
                                navigateToHome()
                            }
                        }

                    }

                    else -> {
                        // Handle other states if needed
                    }
                }
            }
        }
    }

    private fun showBiometricLoginConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Enable Biometric Login")
            .setMessage("Do you want to use biometric login for future logins?")
            .setPositiveButton("Yes") { dialog, _ ->
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val userId = viewModel.currentUser?.uid ?: ""
                viewModel.saveCredentials(email, password, userId)
                navigateToHome()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                navigateToHome()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
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