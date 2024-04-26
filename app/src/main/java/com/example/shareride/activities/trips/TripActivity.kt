package com.example.shareride.activities.trips

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R
import com.example.shareride.activities.custom_cards.CustomTripCard
import com.example.shareride.activities.mainActivity.fragments.ViewModelMainActivity

class TripActivity : ComponentActivity() {

    private lateinit var tripAdapter: CustomTripCard




    override fun onCreate(savedInstanceState: Bundle?) {
         val viewModel: ViewModelMainActivity by viewModels()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        val trip_cards = findViewById<RecyclerView>(R.id.automobileTrips)
        val title = findViewById<TextView>(R.id.pickYourRideTO)

        val destination = viewModel.destination.value
        println(destination)

        if (destination != null) {
                title.text = "Pick your ride to: $destination"

        } else {
                title.text = "There are no rides available"

        }


        trip_cards.layoutManager = LinearLayoutManager(this)


        tripAdapter = CustomTripCard(emptyList(), "Car")
        trip_cards.adapter = tripAdapter

        viewModel._tripsLvdata.observe(this, Observer {
            trips ->
            tripAdapter.updateTrips(trips)
        })


        viewModel.fetchTrips()


    }
}