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




}