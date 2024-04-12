package com.example.shareride.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shareride.cache.TripCache
import com.example.shareride.clases.Trip
import com.example.shareride.repository.TripRepository
import java.util.Date

class TripPersistence {
    private val repository = TripRepository()

    private val _tripByIdLvdata = MutableLiveData<Trip?>()
    val tripById: LiveData<Trip?> = _tripByIdLvdata


    fun getTrip(tripid:Int, currentDate: Date){

        val cachedTrip = TripCache.tripMap[tripid]
        val lst_update = TripCache.tripMap_update[tripid]
        if (cachedTrip!= null && lst_update != null) {
            val differenceInMillis = currentDate.time - lst_update.time
            val thirtySecondsInMillis = 30 * 1000

            if (differenceInMillis <= thirtySecondsInMillis) {
                _tripByIdLvdata.postValue(cachedTrip)
                return
            }

        }

        val response = repository.getTrip(tripid)
        _tripByIdLvdata.postValue(response)

        response?.let {
            TripCache.tripMap[tripid] = it
            TripCache.tripMap_update[tripid] = currentDate
        }}
}