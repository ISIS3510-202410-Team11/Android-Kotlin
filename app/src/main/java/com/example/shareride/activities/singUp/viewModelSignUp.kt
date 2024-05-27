package com.example.shareride.activities.singUp


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class viewModelSignUp(private val networkConnectivityObserver: NetworkConnectivityObserver) :
    ViewModel() {
    var inputpassword: String = ""
    var inputEmail: String = ""
    var inputText: String = ""
    var pending_singup = false


    val connectivityStatus: MutableLiveData<ConnectivityObserver.Status> = MutableLiveData()

    init {
        observeNetworkConnectivity()
    }

    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                connectivityStatus.postValue(status)
            }
        }
    }


    fun change_password(new_pass: String) {
        inputpassword = new_pass
    }

    fun change_email(new_email: String) {
        inputEmail = new_email
    }

    fun change_name(new_name: String) {

        Firebase.analytics.logEvent("Close_sign_up", null)

        inputText = new_name
    }
}