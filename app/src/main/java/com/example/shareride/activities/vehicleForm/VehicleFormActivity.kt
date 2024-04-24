package com.example.shareride.activities.vehicleForm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.shareride.R
import com.example.shareride.activities.mainActivity.MainActivityPassenger
import com.example.shareride.databinding.ActivityVehicleFormBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.example.shareride.StartActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VehicleFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleFormBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var colorList: ArrayList<String>
    private lateinit var markList: ArrayList<String>
    private lateinit var markAdapter: ArrayAdapter<String>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleFormBinding.inflate(layoutInflater)
        colorList = ArrayList()
        markList = ArrayList()

        fetchDataFromFirebase()

        markAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, markList)
        binding.autoCompleteTextView.threshold = 0 // Show all suggestions when touched
        binding.autoCompleteTextView.setAdapter(markAdapter)

        binding.autoCompleteTextView.setOnTouchListener { v, event ->
            binding.autoCompleteTextView.showDropDown()
            false
        }

        setContentView(binding.root)
    }

    private fun fetchDataFromFirebase() {
        database = FirebaseDatabase.getInstance().getReference("/acceptedVehiclesForm/")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val jsonObject = dataSnapshot.value as? Map<*, *>?

                if (jsonObject != null && jsonObject.containsKey("Color") && jsonObject.containsKey("Mark")) {
                    colorList = (jsonObject["Color"] as? ArrayList<String>)!!
                    markList = (jsonObject["Mark"] as? ArrayList<String>)!!
                    markAdapter.clear()
                    markAdapter.addAll(markList)
                    markAdapter.notifyDataSetChanged()

                } else {
                    println("JSON object is null or does not contain 'Color' key.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
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

        val vehicle = Vehicle(type, plate, reference, color)

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
