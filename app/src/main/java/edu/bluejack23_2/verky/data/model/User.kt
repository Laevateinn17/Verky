package edu.bluejack23_2.verky.data.model

import java.util.Date

data class User(
    var id: String? = "",
    var name: String = "",
    var email: String = "",
    var dob: String = "",
    var gender: String = "",
    var religion: String = "",
    var interest: List<String> = emptyList(),
    var incognito_mode: Boolean = false,
    var profile_picture: String = "",
    var gallery_picture: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", "", "", emptyList(), false, "", emptyList())
}