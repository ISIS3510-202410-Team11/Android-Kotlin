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

    private val tripsRet: MutableList<Trip> = mutableListOf()




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



    fun getTripsWhileOffline(destination: String, origin: String): List<Trip> {
        val trips = mutableListOf<Trip>()
        if (TripCache.origin == origin && TripCache.destination == destination) {
            for (tripId in TripCache.idValid) {
                TripCache.tripMap[tripId]?.let {
                    trips.add(it)
                }
            }
        }
        return trips
    }



    fun getTrips(count: Int, destination: String, origin:String,callback: (List<Trip>?) -> Unit) {




        repository.getTrips(count, destination, origin){ trips ->

            TripCache.origin = origin
            TripCache.destination = destination
            TripCache.clearCache()

            if(trips != null) {

                for (trip in trips) {
                    TripCache.idValid.add(trip.id)





                    if (TripCache.tripMap[trip.id] == null) {
                        println("cached")

                        TripCache.tripMap[trip.id] = trip
                        TripCache.tripMap_update[trip.id] = getCurrentDateTime()


                    }
                    else{
                        println("already cahced")

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