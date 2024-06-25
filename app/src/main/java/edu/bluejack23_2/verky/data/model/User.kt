package edu.bluejack23_2.verky.data.model

import java.util.Date

data class User(
    val id: String? = "",
    val name: String = "",
    val email: String = "",
    val dob: String = "",
    val gender: String = "",
    val religion: String = "",
    val activities: List<String> = emptyList(),
    val incognito_mode: Boolean = false,
    val profile_picture: String = "",
    val gallery_picture: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", "", "", emptyList(), false, "", emptyList())
}