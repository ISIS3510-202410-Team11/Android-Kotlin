package com.example.shareride.activities.vehicleForm

import Form
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
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
import com.example.shareride.clases.ConnectivityReceiver
import com.example.shareride.clases.Vehicle
import com.example.shareride.databinding.ActivityVehicleFormBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

class VehicleFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleFormBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var colorList: ArrayList<String>
    private lateinit var markList: ArrayList<String>
    private lateinit var markAdapter: ArrayAdapter<String>
    private lateinit var colorAdapter: ArrayAdapter<String>
    private lateinit var colorCache: LruCache<String, ArrayList<String>>
    private lateinit var markCache: LruCache<String, ArrayList<String>>
    private lateinit var connectivityReceiver: ConnectivityReceiver

    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitmap: Bitmap? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeFirebase()
        initializeCache()
        initializeUI()
        fetchDataFromCacheAndCheckForUpdates()

        val cancelButton: ImageButton = findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener {
            navigateToStartActivity()
        }

        binding.registerVehicle.setOnClickListener {
            registerVehicle()
        }

        binding.takePictureButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        registerConnectivityReceiver()
    }

    private fun initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    private fun initializeCache() {
        val maxCacheSize = (Runtime.getRuntime().maxMemory() / 1024).toInt() / 8
        colorCache = LruCache(maxCacheSize)
        markCache = LruCache(maxCacheSize)
    }

    private fun initializeUI() {
        colorList = ArrayList()
        markList = ArrayList()

        markAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, markList)
        colorAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, colorList)

        with(binding.autoCompleteTextViewMark) {
            threshold = 0
            setAdapter(markAdapter)
            setOnTouchListener { _, _ ->
                showDropDown()
                false
            }
        }

        with(binding.autoCompleteTextViewColor) {
            threshold = 0
            setAdapter(colorAdapter)
            setOnTouchListener { _, _ ->
                showDropDown()
                false
            }
        }
    }

    private fun fetchDataFromCacheAndCheckForUpdates() {
        val cachedColorList = colorCache.get("colorList")
        val cachedMarkList = markCache.get("markList")

        if (cachedColorList != null && cachedMarkList != null) {
            populateUIWithLatestData(cachedColorList, cachedMarkList)
        }

        checkForUpdatesInBackground()
    }

    private fun checkForUpdatesInBackground() {
        firestore.collection("acceptedVehiclesForm").get()
            .addOnSuccessListener { querySnapshot ->
                val document = querySnapshot.documents.firstOrNull()
                document?.let {
                    val colorListFromFirestore = it.get("Color") as? ArrayList<String>
                    val markListFromFirestore = it.get("Mark") as? ArrayList<String>
                    if (colorListFromFirestore != null && markListFromFirestore != null) {
                        populateUIWithLatestData(colorListFromFirestore, markListFromFirestore)
                        updateCache(colorListFromFirestore, markListFromFirestore)
                    } else {
                        println("Document does not contain 'Color' or 'Mark' keys.")
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    private fun populateUIWithLatestData(colorList: ArrayList<String>, markList: ArrayList<String>) {
        runOnUiThread {
            this.markList.clear()
            this.markList.addAll(markList)
            markAdapter.notifyDataSetChanged()

            this.colorList.clear()
            this.colorList.addAll(colorList)
            colorAdapter.notifyDataSetChanged()
        }
    }

    private fun updateCache(colorList: ArrayList<String>, markList: ArrayList<String>) {
        colorCache.put("colorList", colorList)
        markCache.put("markList", markList)
    }

    private fun registerVehicle() {
        val userId = firebaseAuth.currentUser?.uid
        val mark = binding.autoCompleteTextViewMark.text.toString().trim()
        val type = binding.typeInput.text.toString().trim()
        val plate = binding.plateInput.text.toString().trim()
        val reference = binding.referenceInput.text.toString().trim()
        val color = binding.autoCompleteTextViewColor.text.toString().trim()

        if (!validateInput(mark, type, plate, reference, color)) return

        val fileName = generateFileName() + ".png"
        val vehicle = Vehicle(mark, type, plate, reference, color, fileName)

        userId?.let {
            if (isNetworkConnected()) {
                firestore.collection("users").document(it).collection("vehicles").add(vehicle)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            saveImageToStorage(fileName)
                            navigateToMainActivityPassenger()
                        } else {
                            Toast.makeText(this, "Vehicle registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                saveVehicleToSharedPreferences(Form(userId, vehicle))
                navigateToMainActivityPassenger()
            }
        }
    }

    private fun validateInput(mark: String, type: String, plate: String, reference: String, color: String): Boolean {
        if (mark.isEmpty() || type.isEmpty() || plate.isEmpty() || reference.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!colorList.contains(color) || !markList.contains(mark)) {
            Toast.makeText(this, "Invalid color or mark selected", Toast.LENGTH_SHORT).show()
            return false
        }

        val platePattern = Regex("[A-Z]{3}[0-9]{3}")
        if (!platePattern.matches(plate)) {
            Toast.makeText(this, "Invalid plate format. Please provide a valid plate.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveVehicleToSharedPreferences(form: Form) {
        val sharedPref = getSharedPreferences("your_pref_name", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("vehicle_key", Gson().toJson(form))
            apply()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
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
            imageBitmap = data?.extras?.get("data") as Bitmap
        }
    }

    private fun saveImageToStorage(fileName: String) {
        val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(directory, fileName)

        try {
            val fileOutputStream = FileOutputStream(file)
            imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateFileName(): String {
        val plate = binding.plateInput.text.toString().trim()
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val combinedString = "$plate-$currentDate"
        return hashString(combinedString)
    }

    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun navigateToStartActivity() {
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }

    private fun navigateToMainActivityPassenger() {
        startActivity(Intent(this, MainActivityPassenger::class.java))
        finish()
    }

    private fun registerConnectivityReceiver() {
        connectivityReceiver = ConnectivityReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }
}