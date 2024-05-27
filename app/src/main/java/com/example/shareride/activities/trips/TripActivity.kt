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
import com.example.shareride.databinding.ActivityTripBinding
import com.example.shareride.databinding.FragmentHomeDriverBinding
import com.google.api.Billing.BillingDestination

class TripActivity : ComponentActivity() {

    private lateinit var tripAdapter: CustomTripCard
    private lateinit var connectivityWarning: LinearLayout
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: viewModelTrips
    lateinit var destination: String
    lateinit var origin: String
    lateinit var trip_cards : RecyclerView
    lateinit var title: TextView
    private lateinit var binding: ActivityTripBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityTripBinding.inflate(layoutInflater)
        setContentView(binding.root)


         networkConnectivityObserver = NetworkConnectivityObserver(applicationContext)
         viewModelFactory = ViewModelFactory(networkConnectivityObserver, this)
         viewModel = ViewModelProvider(this, viewModelFactory).get(viewModelTrips::class.java)


        destination = intent.getStringExtra("DESTINATION").toString()
        origin = intent.getStringExtra("ORIGIN").toString()

        trip_cards = binding.automobileTrips
        title = binding.pickYourRideTO
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