package com.example.shareride.activities.logIn

import NotificationHelper
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.shareride.R
import com.example.shareride.StartActivity
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.activities.mainActivity.MainActivityPassenger
import com.example.shareride.activities.forgotPasswordActivity.ForgotPasswordActivity
import com.example.shareride.activities.mainActivity.fragments.ViewModelMainActivity
import com.example.shareride.activities.singUp.ViewModelFactory
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import com.google.firebase.auth.FirebaseAuth


import com.example.shareride.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModelMainActivity: ViewModelMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkConnectivityObserver = NetworkConnectivityObserver(this) // Asume que tienes una instancia válida
        val factory = ViewModelFactory(networkConnectivityObserver, applicationContext)
        viewModelMainActivity = ViewModelProvider(this, factory).get(ViewModelMainActivity::class.java)


        println(viewModelMainActivity._locationsLVdata.value)



        val closeButton: ImageButton = findViewById(R.id.cancel_button)
        closeButton.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.textView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        binding.singUpbutton2.setOnClickListener {
            val email = binding.emailInputSingUp.text.toString()
            val pass = binding.passwordInputSingUp.text.toString()
            val isNetwork = isNetworkConnected()
            if (isNetwork) {
                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivityPassenger::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else{
                val notificationHelper = NotificationHelper(this)
                val notification = notificationHelper.buildNotification("No Internet Connection", "You dont seem to have connection network, please try again later")
                notificationHelper.notify(3, notification)
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivityPassenger::class.java)
            startActivity(intent)
        }
    }
}