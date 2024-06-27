package edu.bluejack23_2.verky.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.Notification
import edu.bluejack23_2.verky.data.notification.NotificationRepository
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    val notificationRepository: NotificationRepository
)
: ViewModel() {

    private val _notificationList = MutableLiveData<List<Notification>>()

    val notificationList: LiveData<List<Notification>>
        get() = _notificationList

    init {
        LoggedUser.getInstance().getUser()?.id?.let { fetchNotificationList(it) }
    }

    private fun fetchNotificationList(userID: String) {
        notificationRepository.getNotificationList(userID) { notification ->
            _notificationList.postValue(notification)
        }
    }

}