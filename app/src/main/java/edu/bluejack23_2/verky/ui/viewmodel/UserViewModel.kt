package edu.bluejack23_2.verky.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.data.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun getPotentialMatch() : User {
        return userRepository.getPotentialMatch()
    }

    suspend fun fetchUser(userID: String) : User {
        return userRepository.getUser(userID)
    }

}