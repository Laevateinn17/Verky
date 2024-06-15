package edu.bluejack23_2.verky.data.model

class LoginUser private constructor() {
    private var user : User? =  null

    fun setUser(user : User){
        this.user = user;
    }

    fun getUser() : User?{
        return user;
    }

    companion object {
        @Volatile
        private var instance: LoginUser? = null

        fun getInstance(): LoginUser =
            instance ?: synchronized(this) {
                instance ?: LoginUser().also { instance = it }
            }
    }
}