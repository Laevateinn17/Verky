package edu.bluejack23_2.verky.data.user

import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.model.User

interface UserRepository {
    suspend fun setLoggedUser(userId : String)
}