package com.example.shareride.activities.mainActivity.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareride.MapBoxAPI.IQLocationAPI
import com.example.shareride.clases.Event
import com.example.shareride.clases.LocationIQResponse
import com.example.shareride.clases.Trip
import com.example.shareride.persistence.AnaliticsPersistence
import com.example.shareride.persistence.TripPersistence
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.mapbox.maps.OfflineManager
import com.mapbox.maps.Style
import com.mapbox.maps.TilesetDescriptorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ViewModelMainActivity  : ViewModel()    {

    var page_name: String = "Home"
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()



    val _isRequestPending = MutableLiveData<Boolean>()
    val isRequestPending: LiveData<Boolean>
        get() = _isRequestPending

    init {
        _isRequestPending.value = false
    }



    private val _isRequestDestinationPending = MutableLiveData<Boolean>()
    val isRequestDestinationPending: LiveData<Boolean>
        get() = _isRequestDestinationPending

    init {
        _isRequestDestinationPending.value = false
    }


    var isaddingloc =false


    private lateinit var analytics: FirebaseAnalytics
    var latitud_me_i:Double = 0.0
    var longitud_me_i:Double =0.0
    var clicks_bf_create=0
    var is_click_create = false

    val offlineManager: OfflineManager = OfflineManager()



    val api_location: IQLocationAPI = IQLocationAPI()



    val origin: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val destination: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }



    // Persistence


     var anPersistence:AnaliticsPersistence = AnaliticsPersistence()
    var tripsPersistence: TripPersistence =  TripPersistence()


     val _tripsLvdata = MutableLiveData<List<Trip?>?>()
    val trips: MutableLiveData<List<Trip?>?> = _tripsLvdata






    fun downloadmap(){
        val region = offlineManager.createTilesetDescriptor(
            TilesetDescriptorOptions.Builder()
                .styleURI(Style.MAPBOX_STREETS)
                .minZoom(0)
                .maxZoom(14)
                .build()
        )
    }


    fun reverse_geocode_destination(longitud: Double, latitud:Double): Boolean {
        var locationIQResponse: String? = null

        if (!_isRequestDestinationPending.value!!) {
            _isRequestDestinationPending.value = true


            val apiKey = "pk.0c90a8ce84e34aafc741efec3190ab55"
            val url = "https://us1.locationiq.com/v1/reverse"
            val queryParams = listOf(
                "key" to apiKey,
                "lat" to latitud.toString(),
                "lon" to longitud.toString(),
                "format" to "json"
            )




            viewModelScope.launch(Dispatchers.IO) {
                url.httpGet(queryParams).responseString { _, response, result ->
                    _isRequestDestinationPending.value = false

                    when (result) {

                        is Result.Success -> {
                            val responseBody = result.get()
                            val gson = Gson()
                            locationIQResponse = gson.fromJson(
                                responseBody,
                                LocationIQResponse::class.java
                            ).display_name.toString()

                            val parts = locationIQResponse?.split(",")
                            if (parts != null) {
                                destination.postValue(parts.take(2).joinToString(","))
                            }


                        }

                        is Result.Failure -> {
                            println("Error al realizar la solicitud ${response.statusCode}  ${response.responseMessage}")

                        }
                    }
                }
            }

        }
        return true


    }
    fun reverse_geocode(longitud: Double, latitud:Double): String? {
        var locationIQResponse: String? = null

        if (!_isRequestPending.value!!) {
            _isRequestPending.value = true


            val apiKey = "pk.0c90a8ce84e34aafc741efec3190ab55"
            val url = "https://us1.locationiq.com/v1/reverse"
            val queryParams = listOf(
                "key" to apiKey,
                "lat" to latitud.toString(),
                "lon" to longitud.toString(),
                "format" to "json"
            )




            viewModelScope.launch(Dispatchers.IO) {

            url.httpGet(queryParams).responseString { _, response, result ->
                _isRequestPending.value = false

                when (result) {

                    is Result.Success -> {
                        val responseBody = result.get()
                        val gson = Gson()
                        locationIQResponse = gson.fromJson(
                            responseBody,
                            LocationIQResponse::class.java
                        ).display_name.toString()

                        val parts = locationIQResponse?.split(",")
                        if (parts != null) {
                            origin.postValue(parts.take(2).joinToString(","))
                        }
                        println(locationIQResponse)
                        println(origin.value)


                    }

                    is Result.Failure -> {
                        println("Error al realizar la solicitud ${response.statusCode}  ${response.responseMessage}")

                    }
                }
            }
        }
        }

            return locationIQResponse


    }
    fun updateOrigin(newValue: String) {
        origin.value = newValue
    }

    fun updateDestination(newValue: String) {
       destination.value = newValue
    }
    fun fetchTrips(){
        viewModelScope.launch {
            val trips = tripsPersistence.getTrips(5, destination.value.toString(), origin.value.toString()) { trips ->
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