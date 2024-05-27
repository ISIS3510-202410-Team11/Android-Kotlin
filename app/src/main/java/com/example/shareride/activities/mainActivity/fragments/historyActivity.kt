package com.example.shareride.activities.mainActivity.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R
import com.example.shareride.clases.Trip
import com.example.shareride.adapters.TripAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class historyActivity : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var tripAdapter: TripAdapter
    private val trips = mutableListOf<Trip>()
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        initializeFirebase()
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        tripAdapter = TripAdapter(trips)
        recyclerView.adapter = tripAdapter

        connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        registerNetworkCallback()


        return view
    }

    private fun initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
    }


    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Network is available, fetch trips from Firestore
                fetchTrips()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                // Network is lost, you can handle this case if needed
            }
        })
    }

    private fun fetchTrips() {
        val userId = firebaseAuth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("finished_trips")
            .whereEqualTo("driver_id", userId)
            .get()
            .addOnSuccessListener { documents ->
                trips.clear()
                for (document in documents) {
                    val trip = document.toObject(Trip::class.java)
                    trips.add(trip)
                }
                tripAdapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connectivityManager.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }
}