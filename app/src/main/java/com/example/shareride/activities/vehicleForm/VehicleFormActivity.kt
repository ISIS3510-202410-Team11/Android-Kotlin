package com.example.shareride.activities.vehicleForm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.LruCache
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shareride.R
import com.example.shareride.StartActivity
import com.example.shareride.databinding.ActivityVehicleFormBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
}
