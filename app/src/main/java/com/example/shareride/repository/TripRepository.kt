package com.example.shareride.repository

import android.widget.Toast
import com.example.shareride.clases.Trip
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase

class TripRepository {

    private lateinit var database: DatabaseReference



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



    fun getTrip(id:Int): Trip? {
        var gotTrip: Trip? = null

        database = FirebaseDatabase.getInstance().getReference("/trips/"+id.toString())
        database.get().continueWith { task ->
            if (task.isSuccessful) {
                val snapshot: DataSnapshot? = task.result
                gotTrip = snapshot?.getValue(Trip::class.java)
            } else {
            }}

        return gotTrip

    }


    fun getTrips(count: Int, destination: String, origin:String ,callback: (List<Trip>?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("/trips")

        reference.orderByChild("destination").equalTo(destination).limitToLast(count)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val tripsList = mutableListOf<Trip>()

                    for (tripSnapshot in dataSnapshot.children) {
                        val trip = tripSnapshot.getValue(Trip::class.java)
                        if (trip != null && trip.departureLoc == origin) {
                            tripsList.add(trip)
                        }
                    }
                    callback(tripsList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null)
                }
            })
    }




}