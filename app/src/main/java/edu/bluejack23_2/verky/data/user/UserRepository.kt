package edu.bluejack23_2.verky.data.user

import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.model.User

interface UserRepository {
    suspend fun setLoggedUser(userId : String)

    suspend fun getReligionData() : List<String>

    suspend fun getInterestData() : List<String>

    suspend fun isEmailAlreadyUsed(email: String): Boolean

    suspend fun getUser(userID : String) : User

    suspend fun getPotentialMatch() : User

    suspend fun addUser(user: User, userID : String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
}