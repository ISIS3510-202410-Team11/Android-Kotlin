package com.example.shareride.clases

data class User(

    val name:String = "",
    val email:String = "",
    val password: String = ""
) {

    constructor(): this(
        name = "", email="", password=""
    )
}