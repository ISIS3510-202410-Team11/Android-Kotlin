package com.example.shareride.activities.singUp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shareride.clases.User
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class viewModelSignUp(private val networkConnectivityObserver: NetworkConnectivityObserver):ViewModel(){
    private val _inputPassword = MutableLiveData<String>()
    val inputPassword: LiveData<String> = _inputPassword

    private val _inputEmail = MutableLiveData<String>()
    val inputEmail: LiveData<String> = _inputEmail

    private val _inputText = MutableLiveData<String>()
    val inputText: LiveData<String> = _inputText


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




    fun change_password(new_pass: String){
        //_inputPassword.value = new_pass
        saveToFirestore("input_password", new_pass)

    }

    fun change_email(new_email: String){
        //_inputEmail.value = new_email
        saveToFirestore("input_email", new_email)

    }
    fun change_name (new_name: String){

        Firebase.analytics.logEvent("Close_sign_up", null)
        saveToFirestore("input_name", new_name)

        //_inputText.value = new_name
    }


    private fun saveToFirestore(field: String, value: Any) {
        val userId = "0"
        val userRef = firestore.collection("user").document(userId)
        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                userRef.update(field, value)
                    .addOnSuccessListener {
                        println("huh")
                    }
                    .addOnFailureListener { e ->
                        println(e.message.toString())

                    }
            } else {
                val data = hashMapOf(field to value)
                userRef.set(data, SetOptions.merge())
                    .addOnSuccessListener {
                        println("ya")
                    }
                    .addOnFailureListener { e ->
                        println(e.message.toString())
                    }
            }
        }
    }

    fun loadUserData(callback: (User) -> Unit) {
        viewModelScope.launch {
            val userRef = firestore.collection("user").document("0")
            userRef.get().addOnSuccessListener { snapshot ->
                if (snapshot != null && snapshot.exists()) {
                     _inputPassword.value = snapshot.getString("input_password") ?: ""
                    _inputEmail.value = snapshot.getString("input_email") ?: ""
                   _inputText.value = snapshot.getString("input_name") ?: ""
                    val newUser = User(_inputText.value.toString(), _inputEmail.value.toString(), _inputPassword.value.toString())
                    callback(newUser)
                } else {
                    println("Snapshot is null or does not exist.")
                }
            }.addOnFailureListener { e ->
                println(e.message.toString())
            }
        }
    }


}