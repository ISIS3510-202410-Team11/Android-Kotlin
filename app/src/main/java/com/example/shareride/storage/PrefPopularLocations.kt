package com.example.shareride.storage

import android.content.Context

class PrefPopularLocations(private val context: Context) {
    companion object {
        private const val SHARED_LOCATION = "shareRideDB"
        private const val KEY_POPULAR_LOCATIONS = "popular_locations"
        private const val KEY_INPUT_PASSWORD = "input_password"
        private const val KEY_INPUT_EMAIL = "input_email"
        private const val KEY_INPUT_NAME = "input_name"
        private const val KEY_PASSWORD_WARN = "password_warn"
        private const val KEY_EMAIL_WARN = "email_warn"
        private const val KEY_NAME_WARN = "name_warn"
        private const val KEY_ORIGIN = "origin"
        private const val KEY_DESTINATION = "destination"
    }

    private val storage = context.getSharedPreferences(SHARED_LOCATION, Context.MODE_PRIVATE)

    fun saveTopNlocations(jsonList: String) {
        storage.edit().putString(KEY_POPULAR_LOCATIONS, jsonList).apply()
    }

    fun getTopNlocations(): String? {
        return storage.getString(KEY_POPULAR_LOCATIONS, "[]")
    }

    fun savePassword(svdpassword: String) {
        storage.edit().putString(KEY_INPUT_PASSWORD, svdpassword).apply()
    }

    fun saveEmail(email: String) {
        storage.edit().putString(KEY_INPUT_EMAIL, email).apply()
    }

    fun saveName(newName: String) {
        storage.edit().putString(KEY_INPUT_NAME, newName).apply()
    }

    fun getEmail(): String? {
        return storage.getString(KEY_INPUT_EMAIL, "")
    }

    fun getName(): String? {
        return storage.getString(KEY_INPUT_NAME, "")
    }

    fun getPassword(): String? {
        return storage.getString(KEY_INPUT_PASSWORD, "")
    }

    fun getEmailWarn(): String? {
        return storage.getString(KEY_EMAIL_WARN, "false")
    }

    fun getNameWarn(): String? {
        return storage.getString(KEY_NAME_WARN, "false")
    }

    fun getPasswordWarn(): String? {
        return storage.getString(KEY_PASSWORD_WARN, "false")
    }

    fun savePasswordWarn(svdpassword: Boolean) {
        storage.edit().putString(KEY_PASSWORD_WARN, svdpassword.toString()).apply()
    }

    fun saveEmailWarn(email: Boolean) {
        storage.edit().putString(KEY_EMAIL_WARN, email.toString()).apply()
    }

    fun saveNameWarn(newName: Boolean) {
        storage.edit().putString(KEY_NAME_WARN, newName.toString()).apply()
    }

    fun saveOrigin(origin: String) {
        storage.edit().putString(KEY_ORIGIN, origin).apply()
    }

    fun getOrigin(): String? {
        return storage.getString(KEY_ORIGIN, "")
    }

    fun saveDestination(destination: String) {
        storage.edit().putString(KEY_DESTINATION, destination).apply()
    }

    fun getDestination(): String? {
        return storage.getString(KEY_DESTINATION, "")
    }
}
