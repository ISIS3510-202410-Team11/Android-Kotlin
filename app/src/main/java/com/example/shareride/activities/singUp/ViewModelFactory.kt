package com.example.shareride.activities.singUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.connectivity.NetworkConnectivityObserver

class ViewModelFactory(private val networkConnectivityObserver: NetworkConnectivityObserver) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModelSignUp::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return viewModelSignUp(networkConnectivityObserver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}