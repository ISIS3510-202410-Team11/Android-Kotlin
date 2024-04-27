package com.example.shareride.activities.singUp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.activities.mainActivity.fragments.ViewModelMainActivity
import com.example.shareride.connectivity.NetworkConnectivityObserver

class ViewModelFactory(private val networkConnectivityObserver: NetworkConnectivityObserver, private  val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(viewModelSignUp::class.java) -> {
                viewModelSignUp(networkConnectivityObserver) as T
            }
            modelClass.isAssignableFrom(ViewModelMainActivity::class.java) -> {
                ViewModelMainActivity(networkConnectivityObserver, context) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}