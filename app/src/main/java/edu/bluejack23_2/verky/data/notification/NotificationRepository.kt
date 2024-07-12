package edu.bluejack23_2.verky.data.notification

import edu.bluejack23_2.verky.data.model.Notification
import edu.bluejack23_2.verky.data.model.User


interface NotificationRepository {
    fun getNotificationList(userID: String, callback:(List<Notification>) -> Unit)

    fun acceptNotification(notif : Notification)

    fun rejectNotification(notif : Notification)

    fun addNotification(notif: Notification)

    fun declineNotification(notif: Notification)

}