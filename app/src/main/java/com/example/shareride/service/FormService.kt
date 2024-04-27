package com.example.shareride.service

import Form
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.IBinder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

class FormService : Service() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate() {
        super.onCreate()
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isNetworkConnected()) {
                uploadFormData()
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun uploadFormData() {
        val sharedPref =
            applicationContext.getSharedPreferences("your_pref_name", Context.MODE_PRIVATE)
        val formDataJson = sharedPref.getString("vehicle_key", null)
        formDataJson?.let {
            val form = Gson().fromJson(it, Form::class.java)
            val userId = form.userId
            val vehicle = form.vehicle

            // Upload vehicle data to the Firebase database under the user's ID
            database.child("users").child(userId).child("vehicles").push().setValue(vehicle)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Remove the saved form data from SharedPreferences after successful upload
                        with(sharedPref.edit()) {
                            remove("vehicle_key")
                            apply()
                        }
                    }
                }
        }
    }
}
