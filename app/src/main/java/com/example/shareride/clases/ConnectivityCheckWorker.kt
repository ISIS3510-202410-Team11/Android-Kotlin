package com.example.shareride.clases

import Form
import NotificationHelper
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.google.firebase.database.FirebaseDatabase

class ConnectivityCheckWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Check network connectivity
        val isConnected = isNetworkConnected(applicationContext)

        // If connected, upload cached vehicle data and show a notification
        if (isConnected) {
            uploadCachedVehicleData(applicationContext)
        }

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun uploadCachedVehicleData(context: Context) {
        val sharedPref = context.getSharedPreferences("your_pref_name", Context.MODE_PRIVATE)
        val formDataJson = sharedPref.getString("vehicle_key", null)
        if (!formDataJson.isNullOrEmpty()) {
            val form = Gson().fromJson(formDataJson, Form::class.java)
            uploadVehicleToFirebase(context, form)
            clearVehicleDataFromSharedPreferences(context)
        }
    }

    private fun uploadVehicleToFirebase(context: Context, form: Form) {
        val database = FirebaseDatabase.getInstance().reference
        val userId = form.userId
        userId?.let {
            database.child("users").child(it).child("vehicles").push().setValue(form.vehicle)
                .addOnCompleteListener { task ->
                    val notificationHelper = NotificationHelper(context)
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

    private fun clearVehicleDataFromSharedPreferences(context: Context) {
        val sharedPref = context.getSharedPreferences("your_pref_name", Context.MODE_PRIVATE)
        sharedPref.edit().remove("vehicle_key").apply()
    }
}
