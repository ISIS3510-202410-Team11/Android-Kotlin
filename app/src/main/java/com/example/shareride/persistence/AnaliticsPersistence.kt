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


    fun persist_clicks_bf_createride(clicks:Int, event:String){

        database = FirebaseDatabase.getInstance().reference.child("analitica").child("clicks")
        val newEventRef = database.push()

        val eventId = newEventRef.key
        logD("ViewMode,","1success")


        val new_event=Event(eventId,event,clicks)

        newEventRef.setValue(new_event).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                logD("ViewMode,","success")


            }
            }
    }


}