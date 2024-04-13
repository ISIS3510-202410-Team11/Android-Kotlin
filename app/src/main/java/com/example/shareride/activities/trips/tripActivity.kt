package com.example.shareride.activities.trips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R
import com.example.shareride.activities.custom_cards.CustomTripCard
import com.example.shareride.activities.mainActivity.fragments.ViewModelMainActivity

class tripActivity : ComponentActivity() {

    private lateinit var tripAdapter: CustomTripCard


    val viewModel: ViewModelMainActivity by lazy {

        ViewModelProvider(this).get(ViewModelMainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        val trip_cards = findViewById<RecyclerView>(R.id.automobileTrips)
        trip_cards.layoutManager = LinearLayoutManager(this)


        tripAdapter = CustomTripCard(emptyList(), "Car") // Inicializa el adaptador con una lista vacía
        trip_cards.adapter = tripAdapter

        viewModel._tripsLvdata.observe(this, Observer {
            trips ->
            tripAdapter.updateTrips(trips)
        })


        viewModel.fetchTrips()


    }
}