package com.example.shareride.cache

import com.example.shareride.clases.Trip
import java.util.Date

object TripCache {

      val tripMap= mutableMapOf<String, Trip>()
      val tripMap_update= mutableMapOf<String, Date>()
      var origin= String()
      var destination = String()
      var idValid: MutableList<String> = mutableListOf()



      fun clearCache() {
            for (i in idValid.indices) {
                  idValid[i] = ""
            }
      }
}