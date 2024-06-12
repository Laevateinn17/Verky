package edu.bluejack23_2.verky.Repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepository {
    private val db : DatabaseReference = FirebaseDatabase.getInstance().reference;
    
}