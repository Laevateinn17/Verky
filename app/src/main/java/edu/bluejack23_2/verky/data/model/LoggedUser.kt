package edu.bluejack23_2.verky.data.model

class LoggedUser private constructor() {
    private var user : User? =  null

    fun setUser(user : User){
        this.user = user;
    }

    fun getUser() : User?{
        return user;
    }

    companion object {
        @Volatile
        private var instance: LoggedUser? = null

        fun getInstance(): LoggedUser =
            instance ?: synchronized(this) {
                instance ?: LoggedUser().also { instance = it }
            }
    }
}