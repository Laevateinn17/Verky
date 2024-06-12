package edu.bluejack23_2.verky.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import edu.bluejack23_2.verky.Listener.AuthListener
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.Repository.UserRepository
import edu.bluejack23_2.verky.ViewModel.LoginViewModel
import edu.bluejack23_2.verky.databinding.ActivityLoginBinding
import edu.bluejack23_2.verky.Util.toast

class LoginActivity : AppCompatActivity(), AuthListener{

    private lateinit var binding : ActivityLoginBinding
    private lateinit var registerLink : TextView
    private lateinit var viewModel: LoginViewModel

    fun event(){
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (UserRepository.checkLoggedIn()) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.loginViewModel = viewModel
        viewModel.authListener = this
    }

    override fun OnStarted() {
        TODO("Not yet implemented")
    }

    override fun OnSuccess() {
        startActivity(Intent(this, DashboardActivity::class.java))
    }

    override fun OnFailure(message: String) {
        toast(message)
    }
}