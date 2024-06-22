package edu.bluejack23_2.verky.data.model

import java.util.Date

data class User(
    val name: String,
    val email: String,
    val dob: Date,
    val gender: String,
    val religion: String,
    val activities: List<String>,
    val incognitoMode: Boolean,
    val profilePicture: String,
    val galleryPicture : List<String>
)