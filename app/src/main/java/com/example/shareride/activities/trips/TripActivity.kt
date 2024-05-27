package com.example.shareride.activities.trips

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
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
import com.example.shareride.activities.singUp.ViewModelFactory
import com.example.shareride.activities.singUp.viewModelSignUp
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver

class TripActivity : ComponentActivity() {

    private lateinit var tripAdapter: CustomTripCard
    private lateinit var connectivityWarning: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)



        val networkConnectivityObserver = NetworkConnectivityObserver(applicationContext)
        val viewModelFactory = ViewModelFactory(networkConnectivityObserver, this)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(viewModelTrips::class.java)


        val destination = intent.getStringExtra("DESTINATION")
        val origin = intent.getStringExtra("ORIGIN")

        val trip_cards = findViewById<RecyclerView>(R.id.automobileTrips)
        val title = findViewById<TextView>(R.id.pickYourRideTO)
        connectivityWarning = findViewById(R.id.offlineWarn)


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

           if (trips.isNullOrEmpty()) {


               title.text = "There are no rides available"
               }
           }
        )



        viewModel.connectivityStatus.observe(this) { status ->
            when (status) {
                ConnectivityObserver.Status.Lost -> {
                    connectivityWarning.visibility = View.VISIBLE
                    viewModel.fetchTripsOfline(destination.toString(), origin.toString())

                }
                ConnectivityObserver.Status.Unavailable -> {

                    connectivityWarning.visibility = View.VISIBLE
                    viewModel.fetchTripsOfline(destination.toString(), origin.toString())

                }

                ConnectivityObserver.Status.Avalilable ->{
                    connectivityWarning.visibility = View.GONE
                    viewModel.fetchTrips(destination.toString(), origin.toString())
                }

                ConnectivityObserver.Status.Losing ->{
                    viewModel.fetchTrips(destination.toString(), origin.toString())


                }



                }

        }



        viewModel.fetchTrips(destination.toString(),origin.toString())





    }
}