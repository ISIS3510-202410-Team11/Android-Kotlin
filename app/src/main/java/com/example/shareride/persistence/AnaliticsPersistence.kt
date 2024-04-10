package com.example.shareride.persistence

import com.example.shareride.clases.Event
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mapbox.maps.logD
import kotlin.math.log

class AnaliticsPersistence {

    private lateinit var database: DatabaseReference


    fun persist_clicks_bf_createride(newEvent: Event){

        database = FirebaseDatabase.getInstance().getReference("/analitica/clicks")

        val newEventRef = database.push()
        println(newEvent.clicks)


        newEventRef.setValue(newEvent)
            .addOnSuccessListener {


            }

    }


}