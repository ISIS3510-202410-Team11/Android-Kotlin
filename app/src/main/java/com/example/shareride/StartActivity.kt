package com.example.shareride

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.activities.singUp.SingUpActivity
import com.example.shareride.activities.logIn.LogInActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shareride.activities.mainActivity.fragments.ViewModelMainActivity
import com.example.shareride.activities.singUp.ViewModelFactory
import com.example.shareride.clases.ConnectivityCheckWorker
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import java.util.concurrent.TimeUnit
private lateinit var viewModelMainActivity: ViewModelMainActivity
class StartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val networkConnectivityObserver = NetworkConnectivityObserver(this) // Asume que tienes una instancia válida
        val factory = ViewModelFactory(networkConnectivityObserver, applicationContext)
        viewModelMainActivity = ViewModelProvider(this, factory).get(ViewModelMainActivity::class.java)


        viewModelMainActivity.connectivityStatus.observe(this){status ->
            when (status) {
                ConnectivityObserver.Status.Unavailable ,  ConnectivityObserver.Status.Lost-> {
                    viewModelMainActivity.getcachePopLocations()

                }
                ConnectivityObserver.Status.Avalilable, ConnectivityObserver.Status.Losing -> {
                    viewModelMainActivity.fetchAndCachePopLocations()
                }
            }


        }




        val singUpButton: Button = findViewById(R.id.singUpbutton)
        val loginInButton: Button = findViewById(R.id.logInButton)

        val workRequest = PeriodicWorkRequestBuilder<ConnectivityCheckWorker>(1, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(workRequest)


        singUpButton.setOnClickListener{
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)


        }

        loginInButton.setOnClickListener {
            val intentLogIN = Intent(this, LogInActivity::class.java)
            startActivity(intentLogIN)
        }



    }
}