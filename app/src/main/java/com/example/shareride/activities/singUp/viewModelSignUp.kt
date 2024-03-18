package com.example.shareride.activities.singUp

import androidx.lifecycle.ViewModel

class viewModelSignUp:ViewModel() {
    var inputpassword: String= ""
    var inputEmail: String = ""
    var inputText: String= ""


    fun change_password(new_pass: String){
        inputpassword = new_pass
    }

    fun change_email(new_email: String){
        inputEmail = new_email
    }
    fun change_name (new_name: String){
        inputText = new_name
    }

}