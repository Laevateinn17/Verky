package edu.bluejack23_2.verky.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.User

class ProfileViewModel : ViewModel() {
    private val _profileData = MutableLiveData<User>()
    val profileData: LiveData<User> get() = _profileData

    fun loadProfile() {
        _profileData.value = LoggedUser.getInstance().getUser();
    }
}