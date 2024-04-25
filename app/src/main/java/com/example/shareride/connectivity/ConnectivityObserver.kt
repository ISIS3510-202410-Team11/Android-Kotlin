package com.example.shareride.connectivity

import android.net.http.UrlRequest.Status
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>
    enum class Status{
        Avalilable,
        Unavailable,
        Losing,
        Lost
    }
}