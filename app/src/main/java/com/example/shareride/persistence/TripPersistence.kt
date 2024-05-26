package com.example.shareride.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shareride.cache.TripCache
import com.example.shareride.clases.Location
import com.example.shareride.clases.Trip
import com.example.shareride.repository.TripRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class TripPersistence {
    private val repository = TripRepository()

    private val _tripByIdLvdata = MutableLiveData<Trip?>()
    val tripById: LiveData<Trip?> = _tripByIdLvdata





    fun getTrip(tripid: String) {
        val currentDate = getCurrentDateTime()
        val cachedTrip = TripCache.tripMap[tripid]
        val lst_update = TripCache.tripMap_update[tripid]
        val thirtySecondsInMillis = 30 * 1000

        if (cachedTrip != null && lst_update != null) {
            val differenceInMillis = currentDate.time - lst_update.time
            if (differenceInMillis <= thirtySecondsInMillis) {
                _tripByIdLvdata.postValue(cachedTrip)
                return
            }
        }

        repository.getTrip(tripid) { trip ->
            trip?.let {
                _tripByIdLvdata.postValue(it)
                TripCache.tripMap[tripid] = it
                TripCache.tripMap_update[tripid] = getCurrentDateTime()
            } ?: run {
                // Handle the case where the trip is not found in Firestore
                _tripByIdLvdata.postValue(null)
            }
        }
    }





    fun getTrips(count: Int, destination: String, origin:String,callback: (List<Trip>?) -> Unit) {




        repository.getTrips(count, destination, origin){ trips ->

            if(trips != null) {

                for (trip in trips) {


                    if (TripCache.tripMap[trip.id] == null) {

                        TripCache.tripMap[trip.id] = trip
                        TripCache.tripMap_update[trip.id] = getCurrentDateTime()


                    }
                }
            }
            callback(trips)

        }
    }


    fun getPopularDestinations(count: Int,callback: (List<String>?) -> Unit) {
        repository.getTripsDestinationOrder(count){trips ->
            callback(trips)
        }
    }


    fun getCurrentDateTime(): Date {
        return Date()
    }

    fun getCurrentDateTimeString(): String {
        val currentDateTime = getCurrentDateTime()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(currentDateTime)
    }
}