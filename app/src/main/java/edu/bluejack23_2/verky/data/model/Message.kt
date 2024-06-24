package edu.bluejack23_2.verky.data.model

data class Message (
    val messageId : String,
    val content : String,
    val status : Boolean,
    val timeStamp : String,
    val userID : String
)