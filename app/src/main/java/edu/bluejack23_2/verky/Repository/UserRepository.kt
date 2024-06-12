package edu.bluejack23_2.verky.Repository

import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack23_2.verky.Activities.DashboardActivity

object UserRepository {
    private val db : DatabaseReference = FirebaseDatabase.getInstance().reference;

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun login(email: String, pass: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }

    fun checkLoggedIn() : Boolean{
        val currentUser = auth.currentUser
        return currentUser != null
    }

}