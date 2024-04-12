package com.example.shareride.repository

import com.example.shareride.clases.Trip
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase

class TripRepository {

    private lateinit var database: DatabaseReference


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



    fun getTrips(count:Int){
        database = FirebaseDatabase.getInstance().getReference("/trips/")
        database.orderByKey().limitToLast(count)

    }


}