package com.example.shareride.cache

import com.example.shareride.clases.Trip
import java.util.Date

object TripCache {

      val tripMap= mutableMapOf<Int, Trip>()
      val tripMap_update= mutableMapOf<Int, Date>()

}