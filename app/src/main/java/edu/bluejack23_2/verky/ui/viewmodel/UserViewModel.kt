package edu.bluejack23_2.verky.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.bluejack23_2.verky.data.model.Notification
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.data.notification.NotificationRepository
import edu.bluejack23_2.verky.data.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    suspend fun getPotentialMatch() : User {
        return userRepository.getPotentialMatch()
    }

    suspend fun fetchUser(userID: String) : User {
        return userRepository.getUser(userID)
    }

    suspend fun updateUser(user : User){
        return userRepository.updateUser(user)
    }

    suspend fun acceptUser(notif : Notification){
        return notificationRepository.addNotification(notif)
    }

    suspend fun declineUser(notif : Notification){
        return notificationRepository.declineNotification(notif)
    }

}