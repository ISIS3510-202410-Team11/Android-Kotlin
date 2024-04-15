package com.example.shareride.MapBoxAPI

import com.example.shareride.clases.LocationIQResponse
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson


class IQLocationAPI {

    fun reverse_geocode(longitud: Double, latitud:Double): String? {
        val apiKey = "pk.0c90a8ce84e34aafc741efec3190ab55"
        val url = "https://us1.locationiq.com/v1/reverse"
        val queryParams = listOf(
            "key" to apiKey,
            "lat" to latitud.toString(),
            "lon" to longitud.toString(),
            "format" to "json"
        )
        var locationIQResponse: String? = null



        url.httpGet(queryParams).responseString { _, response, result ->

            when (result) {

                is Result.Success ->{
                    val responseBody = result.get()
                    val gson = Gson()
                    println(gson)
                     locationIQResponse = gson.fromJson(responseBody, LocationIQResponse::class.java).display_name.toString()
                    println(locationIQResponse)


                }
                is Result.Failure ->{
                    println("Error al realizar la solicitud")

                }
            }
        }


        return locationIQResponse


    }
}