package com.example.shareride.activities.mainActivity.fragments

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareride.clases.Event
import com.example.shareride.clases.Location
import com.example.shareride.clases.LocationIQResponse
import com.example.shareride.clases.Trip
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import com.example.shareride.persistence.AnaliticsPersistence
import com.example.shareride.persistence.TripPersistence
import com.example.shareride.storage.PrefPopularLocations
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject


class ViewModelMainActivity (private val networkConnectivityObserver: NetworkConnectivityObserver, private val context: Context):ViewModel(){

    var page_name: String = "Home"
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()



    val _isRequestPending = MutableLiveData<Boolean>()
    val isRequestPending: LiveData<Boolean>
        get() = _isRequestPending

    init {
        _isRequestPending.value = false
    }



    val _isRequestDestinationPending = MutableLiveData<Boolean>()
    val isRequestDestinationPending: LiveData<Boolean>
        get() = _isRequestDestinationPending

    init {
        _isRequestDestinationPending.value = false
    }


    val livelatitudDestination: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }

    val livelaongitudDestination: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }


    var isaddingloc =false


    private lateinit var analytics: FirebaseAnalytics
    var latitud_me_i:Double = 0.0
    var longitud_me_i:Double =0.0
    var clicks_bf_create=0
    var is_click_create = false







    val origin: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val destination: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }



    val _ispendingPOPloc = MutableLiveData<Boolean>()

    val isPenddingPopLocations: LiveData<Boolean>
        get() = _ispendingPOPloc

    init {
        _ispendingPOPloc.value = false
    }


    // Persistence


     var anPersistence:AnaliticsPersistence = AnaliticsPersistence()
    var tripsPersistence: TripPersistence =  TripPersistence()


     val _tripsLvdata = MutableLiveData<List<Trip?>?>()
    val trips: MutableLiveData<List<Trip?>?> = _tripsLvdata



    val _locationsLVdata = MutableLiveData<List<String?>?>()
    val poplocations: MutableLiveData<List<String?>?> = _locationsLVdata




    val connectivityStatus: MutableLiveData<ConnectivityObserver.Status> = MutableLiveData()

    init {
        observeNetworkConnectivity()
    }

    private val prefPopularLocations = PrefPopularLocations(context)

    fun getcachePopLocations(callback: (List<String>?) -> Unit) {

        val cachedLocations = prefPopularLocations.getTopNlocations()

        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        println(cachedLocations.toString())

        val locationsList: List<String> = if (cachedLocations.toString() != "null") {
            println("wtf")
            gson.fromJson(cachedLocations, type)
        } else {
            emptyList()
        }

        _locationsLVdata.value = locationsList
        callback(locationsList)

    }

    fun fetchAndCachePopLocations(callback: (List<String>?) -> Unit) {
        getMostpopularDestination(count = 5) { location ->
            _locationsLVdata.postValue(location)

            viewModelScope.launch(Dispatchers.IO) {
                val gson = Gson()
                val jsonLocations = gson.toJson(location)
                prefPopularLocations.saveTopNlocations(jsonLocations)
            }

            callback(location)
        }
    }

    fun getMostpopularDestination(count: Int, callback: (List<String>?) -> Unit) {
        _ispendingPOPloc.value = true

        tripsPersistence.getPopularDestinations(count) { locations ->
            _ispendingPOPloc.value = false
            callback(locations)
        }
    }
    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                connectivityStatus.postValue(status)
            }
        }
    }






    fun geocode_location (location:String, callback: (Location?, ) -> Unit){




        val apiKey = "pk.0c90a8ce84e34aafc741efec3190ab55"
        val url = "https://us1.locationiq.com/v1/reverse"
        val queryParams = listOf(
            "key" to apiKey,
            "q" to location,
            "format" to "json"
        )

        viewModelScope.launch(Dispatchers.IO) {
            val (_, _, result) = url.httpGet(queryParams).responseString()

            when (result) {
                is Result.Failure -> {
                    // Handle error
                    _isRequestDestinationPending.postValue(false)
                    callback(null)
                }
                is Result.Success -> {
                    val data = result.get()
                    try {
                        val json = JSONObject(data)
                        val lat = json.getString("lat")
                        val lon = json.getString("lon")
                        val displayName = json.getString("display_name")

                        val location = Location(
                            latitud = lat.toDoubleOrNull() ?: 0.0,
                            longitud = lon.toDoubleOrNull() ?: 0.0,
                            destination = location
                        )

                        callback(location)
                    } catch (e: Exception) {
                        callback(null)
                    }
                }
            }
        }
    }


    fun reverse_geocode_destination(longitud: Double, latitud:Double): Boolean {

        if ( _isRequestDestinationPending.value == true) {
            return false

        }


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
                try {
                    val (_, _, result) = url.httpGet(queryParams).responseString()

                    when (result) {
                        is Result.Success -> {
                            val responseBody = result.get()
                            val gson = Gson()
                            val locationIQResponse = gson.fromJson(
                                responseBody,
                                LocationIQResponse::class.java
                            ).display_name.toString()

                            val parts = locationIQResponse.split(",")
                            val formattedAddress = parts.take(2).joinToString(",")
                            destination.postValue(formattedAddress)
                        }
                        is Result.Failure -> {
                            println("Error al realizar la solicitud ${result.error}")
                        }
                    }
                } finally {
                    _isRequestDestinationPending.postValue(false)  // Detener animación de carga
                }
            }
        return false
        }

    fun reverse_geocode(longitud: Double, latitud:Double): String? {
        var locationIQResponse: String? = null

        if (_isRequestPending.value== true ) {
            return ""
        }

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
            try {
                val (_, response, result) = url.httpGet(queryParams).responseString()

                when (result) {
                    is Result.Success -> {
                        val responseBody = result.get()
                        val gson = Gson()
                        val locationIQResponse = gson.fromJson(
                            responseBody,
                            LocationIQResponse::class.java
                        ).display_name.toString()
                        val parts = locationIQResponse?.split(",")

                        if (parts != null) {
                            origin.postValue(parts.take(2).joinToString(","))
                        }
                    }
                    is Result.Failure -> {
                        println("Error al realizar la solicitud ${response.statusCode} ${response.responseMessage}")
                    }
                }
            } finally {
                _isRequestPending.postValue(false) // Marcar que la solicitud ha terminado
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
    init {
        _isSwitchChecked.value = true
    }

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