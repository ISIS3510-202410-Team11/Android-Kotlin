package com.example.shareride.activities.vehicleForm

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.LruCache
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shareride.R
import com.example.shareride.StartActivity
import com.example.shareride.activities.mainActivity.MainActivityPassenger
import com.example.shareride.databinding.ActivityVehicleFormBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import java.io.FileOutputStream

class VehicleFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleFormBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var colorList: ArrayList<String>
    private lateinit var markList: ArrayList<String>
    private lateinit var markAdapter: ArrayAdapter<String>
    private lateinit var colorAdapter: ArrayAdapter<String>
    private lateinit var colorCache: LruCache<String, ArrayList<String>>
    private lateinit var markCache: LruCache<String, ArrayList<String>>

    private val REQUEST_IMAGE_CAPTURE = 1

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("/acceptedVehiclesForm/")

        // Initialize caches
        val maxCacheSize = (Runtime.getRuntime().maxMemory() / 1024).toInt() / 8
        colorCache = LruCache(maxCacheSize)
        markCache = LruCache(maxCacheSize)

        // Initialize lists and adapter
        colorList = ArrayList()
        markList = ArrayList()

        markAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, markList)
        colorAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, colorList)

        binding.autoCompleteTextViewMark.threshold = 0 // Show all suggestions when touched
        binding.autoCompleteTextViewMark.setAdapter(markAdapter)

        binding.autoCompleteTextViewColor.threshold = 0 // Show all suggestions when touched
        binding.autoCompleteTextViewColor.setAdapter(colorAdapter)

        binding.autoCompleteTextViewMark.setOnTouchListener { v, event ->
            binding.autoCompleteTextViewMark.showDropDown()
            false
        }
        binding.autoCompleteTextViewColor.setOnTouchListener { v, event ->
            binding.autoCompleteTextViewColor.showDropDown()
            false
        }

        // Fetch data from cache and check for updates
        fetchDataFromCacheAndCheckForUpdates()

        val cancelButton: ImageButton = findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener {
            // Navigate back to the profile view
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }

        binding.registerVehicle.setOnClickListener {
            // Register the vehicle and save it in the Firebase database
            registerVehicle()
        }

        binding.takePictureButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun populateUIWithLatestData(colorList: ArrayList<String>, markList: ArrayList<String>) {
        // Update UI with latest data from network
        runOnUiThread {
            this.markList.clear()
            this.markList.addAll(markList)
            markAdapter.notifyDataSetChanged()

            this.colorList.clear()
            this.colorList.addAll(colorList)
            colorAdapter.notifyDataSetChanged()
        }
    }

    private fun fetchDataFromCacheAndCheckForUpdates() {
        val cachedColorList = colorCache.get("colorList")
        val cachedMarkList = markCache.get("markList")

        if (cachedColorList != null && cachedMarkList != null) {
            // If cached data is available, populate the UI with it
            populateUIWithLatestData(cachedColorList, cachedMarkList)
        }

        // Now check for updates in the background
        checkForUpdatesInBackground()
    }

    private fun checkForUpdatesInBackground() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val jsonObject = dataSnapshot.value as? Map<*, *>?

                if (jsonObject != null && jsonObject.containsKey("Color") && jsonObject.containsKey("Mark")) {
                    val colorListFromFirebase = (jsonObject["Color"] as? ArrayList<String>)!!
                    val markListFromFirebase = (jsonObject["Mark"] as? ArrayList<String>)!!
                    populateUIWithLatestData(colorListFromFirebase, markListFromFirebase)
                    updateCache(colorListFromFirebase, markListFromFirebase)

                } else {
                    println("JSON object is null or does not contain 'Color' key.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateCache(colorList: ArrayList<String>, markList: ArrayList<String>) {
        colorCache.put("colorList", colorList)
        markCache.put("markList", markList)
    }

    private fun registerVehicle() {
        database = FirebaseDatabase.getInstance().reference
        var userId = firebaseAuth.currentUser?.uid
        val mark = binding.autoCompleteTextViewMark.text.toString().trim()
        val type = binding.typeInput.text.toString().trim()
        val plate = binding.plateInput.text.toString().trim()
        val reference = binding.referenceInput.text.toString().trim()
        val color = binding.autoCompleteTextViewColor.text.toString().trim()

        // Checking for empty values
        if (mark.isEmpty() || type.isEmpty() || plate.isEmpty() || reference.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        //Checking for valid values
        if (!colorList.contains(color) || !markList.contains(mark)) {
            Toast.makeText(this, "Invalid color or mark selected", Toast.LENGTH_SHORT).show()
            return
        }

        val platePattern = Regex("[A-Z]{3}[0-9]{3}")
        if (!platePattern.matches(plate)) {
            Toast.makeText(this, "Invalid plate format. Please provide a valid plate.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new vehicle object
        val vehicle = Vehicle(mark, type, plate, reference, color)
        userId = "RKtI9Ep1e9daaITIMXIyKasi3pr2" //TODO: Delete this when Sign Up is working again.
        // Save the vehicle to the Firebase database under the user's ID
        userId?.let {
            database.child("users").child(it).child("vehicles").push().setValue(vehicle)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivityPassenger::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Vehicle registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            saveImageToStorage(imageBitmap)
        }
    }

    private fun saveImageToStorage(bitmap: Bitmap) {
        val fileName = "my_picture.jpg"
        val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(directory, fileName)

        try {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
}

data class Vehicle(
    val mark: String,
    val type: String,
    val plate: String,
    val reference: String,
    val color: String
)
