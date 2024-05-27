package com.example.shareride.activities.singUp

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareride.clases.User
import com.example.shareride.clases.Warnings
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import com.example.shareride.storage.PrefPopularLocations
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class viewModelSignUp(private val networkConnectivityObserver: NetworkConnectivityObserver,private val context: Context):ViewModel(){
    private val _inputPassword = MutableLiveData<String>()
    val inputPassword: LiveData<String> = _inputPassword

    private val _inputEmail = MutableLiveData<String>()
    val inputEmail: LiveData<String> = _inputEmail

    private val _inputText = MutableLiveData<String>()
    val inputText: LiveData<String> = _inputText
    private val prefPopularLocations = PrefPopularLocations(context)

    var match_mail: Boolean= false
    var match_pass: Boolean= false
    var match_name: Boolean= false


    var pending_singup = false

     lateinit var newUser : User
    private val firestore: FirebaseFirestore

    init {


        firestore = FirebaseFirestore.getInstance()
    }










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




    fun change_password(new_pass: String, warn: Boolean){
        //_inputPassword.value = new_pass
        prefPopularLocations.savePassword( new_pass)
        prefPopularLocations.savePasswordWarn(warn)

    }

    fun change_email(new_email: String, warn: Boolean){
        //_inputEmail.value = new_email
        prefPopularLocations.saveEmail( new_email)
        prefPopularLocations.saveEmailWarn(warn)


    }
    fun change_name (new_name: String, warn: Boolean){

        Firebase.analytics.logEvent("Close_sign_up", null)
        prefPopularLocations.saveName( new_name)
        prefPopularLocations.saveEmailWarn(warn)

        //_inputText.value = new_name
    }





    fun loadUserData(callback: (User) -> Unit) {
        viewModelScope.launch {

                     _inputPassword.value = prefPopularLocations.getPassword()
                    _inputEmail.value = prefPopularLocations.getEmail()
                   _inputText.value = prefPopularLocations.getName()
                     newUser = User(_inputText.value.toString(), _inputEmail.value.toString(), _inputPassword.value.toString())
            println(prefPopularLocations.getPassword())
            println(  _inputEmail.value )

            callback(newUser)

            }
        }
    fun loadWarning(callback: (Warnings) -> Unit) {
        viewModelScope.launch {

            match_mail = prefPopularLocations.getEmailWarn().toBoolean()
            match_pass = prefPopularLocations.getPasswordWarn().toBoolean()
            match_name = prefPopularLocations.getNameWarn().toBoolean()
            val active_warnings = Warnings(match_mail, match_pass, match_name)

            callback(active_warnings)

        }
    }


}