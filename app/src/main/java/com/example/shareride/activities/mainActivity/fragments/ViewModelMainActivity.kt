package com.example.shareride.activities.mainActivity.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareride.clases.Event
import com.example.shareride.clases.Trip
import com.example.shareride.persistence.AnaliticsPersistence
import com.example.shareride.persistence.TripPersistence
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class ViewModelMainActivity  : ViewModel()    {

    var page_name: String = "Home"
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()



    private lateinit var analytics: FirebaseAnalytics
    var latitud_me_i:Double = 0.0
    var longitud_me_i:Double =0.0
    var clicks_bf_create=0
    var is_click_create = false

    var destination = "CIUDAD B"
    var origin = "CIUDAD A"

    // Persistence


     var anPersistence:AnaliticsPersistence = AnaliticsPersistence()
    var tripsPersistence: TripPersistence =  TripPersistence()


     val _tripsLvdata = MutableLiveData<List<Trip?>?>()
    val trips: MutableLiveData<List<Trip?>?> = _tripsLvdata



    fun fetchTrips(){
        viewModelScope.launch {
            val trips = tripsPersistence.getTrips(5, destination, origin) { trips ->
                if(trips!= null){
                    _tripsLvdata.value = trips

                }
            }
        }
    }



    val _isSwitchChecked = MutableLiveData<Boolean>()
    val isSwitchChecked: LiveData<Boolean>
        get() = _isSwitchChecked

    fun change_pg_name(newpage:String){
        page_name = newpage

    }



    // Function to update the LiveData
    fun setSwitchChecked(isChecked: Boolean) {
        _isSwitchChecked.value = isChecked
    }




    fun set_my_location(latitud:Double, logitud:Double){
        latitud_me_i = latitud
        longitud_me_i = logitud
    }

    fun clicks_bf_createride(action: String){


        if (is_click_create){
        }
        else if(!is_click_create && action != "create"  ){

            clicks_bf_create+=1

        } else if( action=="create" && !is_click_create){



            is_click_create=true



            clicks_bf_create+=1

            val params = Bundle().apply {
                putInt("clicks", clicks_bf_create.toInt())
            }

            analytics = Firebase.analytics
            analytics.logEvent("First_time_create_ride"){ param("Clicks",clicks_bf_create.toDouble())}
            val newEvent : Event=Event("First_time_create_ride", clicks_bf_create.toInt())
            anPersistence.persist_clicks_bf_createride(newEvent)



        } else {
        }

    }


    fun singOut(){
        firebaseAuth.signOut()


    }







}