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