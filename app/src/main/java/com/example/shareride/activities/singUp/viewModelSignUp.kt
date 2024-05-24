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
        prefPopularLocations.save_password( new_pass)
        prefPopularLocations.save_assword_warn(warn)

    }

    fun change_email(new_email: String, warn: Boolean){
        //_inputEmail.value = new_email
        prefPopularLocations.save_email( new_email)
        prefPopularLocations.save_email_warn(warn)


    }
    fun change_name (new_name: String, warn: Boolean){

        Firebase.analytics.logEvent("Close_sign_up", null)
        prefPopularLocations.save_name( new_name)
        prefPopularLocations.save_name_warn(warn)

        //_inputText.value = new_name
    }





    fun loadUserData(callback: (User) -> Unit) {
        viewModelScope.launch {

                     _inputPassword.value = prefPopularLocations.get_password()
                    _inputEmail.value = prefPopularLocations.get_email()
                   _inputText.value = prefPopularLocations.get_name()
                    val newUser = User(_inputText.value.toString(), _inputEmail.value.toString(), _inputPassword.value.toString())
            println(prefPopularLocations.get_password())
            println(  _inputEmail.value )

            callback(newUser)

            }
        }
    fun loadWarning(callback: (Warnings) -> Unit) {
        viewModelScope.launch {

            match_mail = prefPopularLocations.get_email_warn().toBoolean()
            match_pass = prefPopularLocations.get_password_warn().toBoolean()
            match_name = prefPopularLocations.get_name_warn().toBoolean()
            val active_warnings = Warnings(match_mail, match_pass, match_name)

            callback(active_warnings)

        }
    }


}