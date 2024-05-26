package com.example.shareride.activities.trips

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareride.clases.Trip
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import com.example.shareride.persistence.TripPersistence
import com.example.shareride.storage.PrefPopularLocations
import kotlinx.coroutines.launch

class viewModelTrips(private val networkConnectivityObserver: NetworkConnectivityObserver, private val context: Context):
    ViewModel() {



    val connectivityStatus: MutableLiveData<ConnectivityObserver.Status> = MutableLiveData()
    private val prefPopularLocations = PrefPopularLocations(context)
    var tripsPersistence: TripPersistence =  TripPersistence()
    val _tripsLvdata = MutableLiveData<List<Trip?>?>()
    val trips: MutableLiveData<List<Trip?>?> = _tripsLvdata


    init {
        observeNetworkConnectivity()



    }
    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                connectivityStatus.postValue(status)
            }
        }
    }



    fun fetchTrips(destination: String, origin: String){
        viewModelScope.launch {
            tripsPersistence.getTrips(5, destination, origin) { trips ->
                if(trips!= null){
                    _tripsLvdata.value = trips
                    println(trips)

                }
            }
        }
    }
}