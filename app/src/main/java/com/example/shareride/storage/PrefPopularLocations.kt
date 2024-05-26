package com.example.shareride.storage

import android.content.Context

class PrefPopularLocations(val context: Context) {
    private val SHARED_LOCATION = "shareRideDB"

    private val storage = context.getSharedPreferences(SHARED_LOCATION, Context.MODE_PRIVATE)

    fun saveTopNlocations(jsonList: String) {
        val editor = storage.edit()
        editor.putString("popular_locations", jsonList.toString())
        editor.apply()
    }

    fun getTopNlocations(): String? {
        return storage.getString("popular_locations", "[]")
    }

    fun save_password(svdpassword: String){
        val editor = storage.edit()
        editor.putString("input_password", svdpassword)
        editor.apply()
    }

    fun save_email(email: String){
        val editor = storage.edit()
        editor.putString("input_email", email)
        editor.apply()
    }
    fun save_name(new_name: String){
        val editor = storage.edit()
        editor.putString("input_name", new_name)
        editor.apply()
    }

    fun get_email(): String? {
        return storage.getString("input_email", "")
    }


    fun get_name(): String? {
        return storage.getString("input_name", "")
    }

    fun get_password(): String? {
        return storage.getString("input_password", "")
    }



    fun get_email_warn(): String? {
        return storage.getString("email_warn", "false")
    }


    fun get_name_warn(): String? {
        return storage.getString("name_warn", "false")
    }

    fun get_password_warn(): String? {
        return storage.getString("password_warn", "false")
    }


    fun save_assword_warn(svdpassword: Boolean){
        val editor = storage.edit()
        editor.putString("password_warn", svdpassword.toString())
        editor.apply()
    }

    fun save_email_warn(email: Boolean){
        val editor = storage.edit()
        editor.putString("email_warn", email.toString())
        editor.apply()
    }
    fun save_name_warn(new_name: Boolean){
        val editor = storage.edit()
        editor.putString("name_warn", new_name.toString())
        editor.apply()
    }

    fun save_origin(origin:String){
        val editor = storage.edit()
        editor.putString("origin",origin.toString())
        editor.apply()
    }

    fun get_origin(): String? {
        return storage.getString("origin", "")

    }


    fun save_destination(destination:String){
        val editor = storage.edit()
        editor.putString("destination",destination.toString())
        editor.apply()
    }

    fun get_destination(): String? {
        return storage.getString("destination", "")

    }
}