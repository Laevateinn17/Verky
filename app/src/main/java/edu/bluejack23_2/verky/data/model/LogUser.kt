package edu.bluejack23_2.verky.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object LogUser {
    private val userLiveData = MutableLiveData<User?>()

    fun getUser(): LiveData<User?> = userLiveData

    fun setUser(user: User?) {
        userLiveData.value = user
    }

    fun clearUser() {
        userLiveData.value = null
    }
}