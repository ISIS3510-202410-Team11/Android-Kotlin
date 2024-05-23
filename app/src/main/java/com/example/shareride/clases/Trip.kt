package com.example.shareride.clases

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Trip(
    val id: String = "",
    val driver_id: String = "",
    val end_location: String = "",
    val passengers: List<String> = emptyList(),
    val route: List<String> = emptyList(),
    val start_location: String = "",
    val start_time: String = "",
    val price : String = ""
) {
    constructor() : this(
        id = "",
        driver_id = "",
        end_location = "",
        passengers = emptyList(),
        route = emptyList(),
        start_location = "",
        start_time = "",
        price = ""
    )
}





