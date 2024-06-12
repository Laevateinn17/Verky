package edu.bluejack23_2.verky.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var emailET : EditText
    private lateinit var passET : EditText
    private lateinit var loginBtn : Button
    private lateinit var registerLink : TextView
    private lateinit var auth : FirebaseAuth

    fun init(){
        emailET = binding.emailEditText
        passET = binding.passwordEditText
        loginBtn = binding.loginButton
        registerLink = binding.RegisterLink
        auth = Firebase.auth
    }

    fun event(){
        loginBtn.setOnClickListener{
            val email = emailET.text.toString()
            val pass = passET.text.toString()

            if(email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Authentication failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
//        auth.signOut()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        event()
    }
}