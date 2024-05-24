package com.example.shareride.clases

data class Warnings (

    val match_mail:Boolean = false,
    val match_pass:Boolean = false,
    val match_name: Boolean = false
){

    constructor(): this(
        match_mail = false, match_pass=false, match_name=false
    )
}