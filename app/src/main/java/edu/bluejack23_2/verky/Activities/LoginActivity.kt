package edu.bluejack23_2.verky.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding;
    private lateinit var emailET : EditText;
    private lateinit var passET : EditText;
    private lateinit var loginBtn : Button;

    fun init(){
        emailET = binding.emailEditText;
        passET = binding.passwordEditText;
        loginBtn = binding.loginButton;
    }

    fun validation(){
        val email = emailET.text.toString();
        val pass = passET.text.toString();

        loginBtn.setOnClickListener{
            if(email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init();
        validation();
    }
}