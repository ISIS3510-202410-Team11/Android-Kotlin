package com.example.shareride.repository

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.shareride.clases.Location
import com.example.shareride.clases.Trip
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TripRepository {

    private lateinit var database: DatabaseReference
    private  lateinit var destinationCountMap : MutableMap<String, Int>
    private  lateinit var mFirestore:  FirebaseFirestore



    fun postTrip(newTrip:Trip): Trip? {



        var createdTrip: Trip? = null
        database = FirebaseDatabase.getInstance().getReference("/trips/")
        val rideId = database.push().key

        rideId?.let {
            database.child(it).setValue(newTrip).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createdTrip =newTrip
                }
                else {
                    println("no se ha creado")


                }
                }
            }
        return createdTrip
    }



    fun getTrip(id:String,callback: (Trip?) -> Unit){
        var gotTrip: Trip? = null
        mFirestore = FirebaseFirestore.getInstance()

        mFirestore.collection("active_trips").whereEqualTo("id",id).get()
            .addOnSuccessListener{querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                val gotTrip = document.toObject(Trip::class.java)
                callback(gotTrip)
            } else {
                callback(null)

            }
        }
            .addOnFailureListener {
                callback(null)

            }
    }


    fun getTripsDestinationOrder(count: Int,callback: (List<String>?) -> Unit){

        mFirestore = FirebaseFirestore.getInstance()

        mFirestore.collection("active_trips")
            .limit(count.toLong())
            .get()

            .addOnSuccessListener { querySnapshot ->
                val destinationCountMap = mutableMapOf<String, Int>()
                val loc_data = mutableMapOf<String, String>()

                querySnapshot.documents.forEach { document ->
                    val trip = document.toObject(Trip::class.java)
                    if (trip != null) {
                        val destination = trip.end_location

                        if (destination != null) {
                            if (destinationCountMap.containsKey(destination)) {
                                destinationCountMap[destination] = destinationCountMap[destination]!! + 1
                            } else {
                                destinationCountMap[destination] = 1
                            }

                            loc_data[destination] = trip.end_location
                        }
                    }
                }

                val popularDestinations = destinationCountMap.entries.sortedByDescending { it.value }
                    .map { it.key }

                val popularLocations = mutableListOf<String>()
                popularDestinations.forEach { destination ->
                    val location = loc_data[destination]
                    if (location != null) {
                        popularLocations.add(location)

                    }
                }
                callback(popularLocations)

            }

            .addOnFailureListener { exception ->
                callback(null)
            }
    }






    fun getTrips(count: Int, destination: String, origin:String ,callback: (List<Trip>?) -> Unit) {

        mFirestore = FirebaseFirestore.getInstance()
        mFirestore.collection("active_trips")
            .whereEqualTo("end_location",destination)
            .limit(count.toLong()).get()

            .addOnSuccessListener { querySnapshot ->
            val tripsList = mutableListOf<Trip>()

            for (document in querySnapshot.documents) {
                val trip = document.toObject(Trip::class.java)
                if (trip != null && trip.start_location == origin) {
                    tripsList.add(trip)
                }
            }
            callback(tripsList)
        }
            .addOnFailureListener { exception ->
                callback(null)
            }
    }




}