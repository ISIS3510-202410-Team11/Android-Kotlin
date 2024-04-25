package com.example.shareride.clases

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Trip(
    val id: Int = 0,
    val destinationlatitud: Double = 0.0,
    val destinationlongitud: Double = 0.0,
    val origin: String = "",
    val destination: String = "",
    val vehicle: String = "",
    val passengers: Int = 0,
    val date: String = "",
    val time: String = "",
    val price: Double = 0.0,
    val closed: Boolean = false,
    val subscribed: Array<String> = emptyArray()
) {
    constructor() : this(
        id = 0,
        destinationlatitud = 0.0,
        destinationlongitud = 0.0,
        origin = "",
        destination = "",
        vehicle = "",
        passengers = 0,
        date = "",
        time = "",
        price = 0.0,
        closed = false,
        subscribed = emptyArray()
    )
}






