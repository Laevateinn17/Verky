package edu.bluejack23_2.verky.data.model

data class Notification(
    val notificationID: String = "",
    val from : String = "",
    val to : String = "",
    val toUser : User? = null,
    var fromUser: User? = null,
) {
    constructor() : this("", "", "", null, null)
}