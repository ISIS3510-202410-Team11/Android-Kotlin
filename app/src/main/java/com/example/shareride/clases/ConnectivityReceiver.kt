import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.example.shareride.clases.Vehicle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            Log.d("ConnectivityReceiver", "hola1")
            val isConnected = isNetworkConnected(context)
            if (isConnected) {
                // If connected to the internet, upload vehicle data from SharedPreferences
                val sharedPref = context?.getSharedPreferences("your_pref_name", Context.MODE_PRIVATE)
                val vehicleDataJson = sharedPref?.getString("vehicle_key", null)
                val vehicle2 = Gson().fromJson(vehicleDataJson, Vehicle::class.java)
                if (!vehicleDataJson.isNullOrEmpty()) {
                    val vehicle = Gson().fromJson(vehicleDataJson, Vehicle::class.java)
                    // Upload vehicle data to Firebase
                    uploadVehicleToFirebase(context, vehicle)
                    // Clear vehicle data from SharedPreferences
                    clearVehicleDataFromSharedPreferences(context)
                }
            }
        }
    }

    private fun isNetworkConnected(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun uploadVehicleToFirebase(context: Context?, vehicle: Vehicle) {
        val database = FirebaseDatabase.getInstance().reference
        val userId = "RKtI9Ep1e9daaITIMXIyKasi3pr2"
        userId?.let {
            database.child("users").child(it).child("vehicles").push().setValue(vehicle)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Vehicle data uploaded successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to upload vehicle data", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    private fun clearVehicleDataFromSharedPreferences(context: Context?) {
        val sharedPref = context?.getSharedPreferences("your_pref_name", Context.MODE_PRIVATE)
        sharedPref?.edit()?.remove("vehicle_key")?.apply()
    }
}
