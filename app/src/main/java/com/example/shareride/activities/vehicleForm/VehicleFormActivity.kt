package com.example.shareride.activities.vehicleForm

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.shareride.R
import com.example.shareride.activities.mainActivity.MainActivityPassenger
import com.example.shareride.databinding.ActivityVehicleFormBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import com.example.shareride.StartActivity


class VehicleFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleFormBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val cancelButton: ImageButton = findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener {
            // Navigate back to the profile view
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }

        binding.registerVehicle.setOnClickListener {
            // Register the vehicle and save it in the Firebase database
            registerVehicle()
            startActivity(Intent(this, MainActivityPassenger::class.java))
            finish()
        }
    }

    private fun registerVehicle() {
        val userId = firebaseAuth.currentUser?.uid
        val type = binding.typeInput.text.toString().trim()
        val plate = binding.plateInput.text.toString().trim()
        val reference = binding.referenceInput.text.toString().trim()
        val color = binding.colorInput.text.toString().trim()

        if (type.isEmpty() || plate.isEmpty() || reference.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new vehicle object
        val vehicle = Vehicle(type, plate, reference, color)

        // Save the vehicle to the Firebase database under the user's ID
        userId?.let {
            databaseReference.child("users").child(it).child("vehicles").push().setValue(vehicle)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        finish()
                    } else {
                        Toast.makeText(this, "Vehicle registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}

// Define a data class to represent a vehicle
data class Vehicle(
    val type: String,
    val plate: String,
    val reference: String,
    val color: String
)
