package edu.bluejack23_2.verky.data.model

data class Notification(
    var notificationID: String = "",
    val from : String = "",
    val to : String = "",
    val toUser : User? = null,
    var fromUser: User? = null,
    val timeStamp : Long? = null
) {
    constructor() : this("", "", "", null, null, null)
}