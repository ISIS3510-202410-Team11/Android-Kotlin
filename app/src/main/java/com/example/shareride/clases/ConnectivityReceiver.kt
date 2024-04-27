package com.example.shareride.clases

import Form
import NotificationHelper
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            Log.d("ConnectivityReceiver", "Internet connection status changed")
            val isConnected = isNetworkConnected(context)
            if (isConnected) {
                Log.d("ConnectivityReceiver", "Internet connected")
                // If connected to the internet, upload vehicle data from SharedPreferences
                uploadCachedVehicleData(context)
            }
        }
    }

    private fun isNetworkConnected(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun uploadCachedVehicleData(context: Context?) {
        val sharedPref = context?.getSharedPreferences("your_pref_name", Context.MODE_PRIVATE)
        val formDataJson = sharedPref?.getString("vehicle_key", null)
        if (!formDataJson.isNullOrEmpty()) {
            val form = Gson().fromJson(formDataJson, Form::class.java)
            uploadVehicleToFirebase(context, form)
            clearVehicleDataFromSharedPreferences(context)
        }
    }

    private fun uploadVehicleToFirebase(context: Context?, form: Form) {
        val database = FirebaseDatabase.getInstance().reference
        val userId = form.userId
        userId?.let {
            database.child("users").child(it).child("vehicles").push().setValue(form.vehicle)
                .addOnCompleteListener { task ->
                    val notificationHelper = NotificationHelper(context!!)
                    if (task.isSuccessful) {
                        val notification = notificationHelper.buildNotification("Upload Status", "Vehicle data uploaded successfully")
                        notificationHelper.notify(1, notification)
                    } else {
                        val notification = notificationHelper.buildNotification("Upload Status", "Failed to upload vehicle data")
                        notificationHelper.notify(2, notification)
                    }
                }
        }
    }

    private fun clearVehicleDataFromSharedPreferences(context: Context?) {
        val sharedPref = context?.getSharedPreferences("your_pref_name", Context.MODE_PRIVATE)
        sharedPref?.edit()?.remove("vehicle_key")?.apply()
        context?.unregisterReceiver(this)
    }
}
