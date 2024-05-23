package com.example.shareride.cache

import com.example.shareride.clases.Trip
import java.util.Date

object TripCache {

      val tripMap= mutableMapOf<String, Trip>()
      val tripMap_update= mutableMapOf<String, Date>()

}