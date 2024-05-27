package com.example.shareride

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequest
import com.example.shareride.activities.singUp.SingUpActivity
import com.example.shareride.activities.logIn.LogInActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shareride.activities.mainActivity.fragments.ViewModelMainActivity
import com.example.shareride.activities.singUp.ViewModelFactory
import com.example.shareride.clases.ConnectivityCheckWorker
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import com.example.shareride.databinding.ActivityStartBinding
import com.example.shareride.databinding.ActivityVehicleFormBinding
import java.util.concurrent.TimeUnit


class StartActivity : AppCompatActivity() {

    private lateinit var viewModelMainActivity: ViewModelMainActivity
    private lateinit var binding: ActivityStartBinding
    private  lateinit var factory: ViewModelFactory
    private lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    private lateinit var workRequest: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)


         networkConnectivityObserver = NetworkConnectivityObserver(this) // Asume que tienes una instancia válida
         factory = ViewModelFactory(networkConnectivityObserver, applicationContext)
        viewModelMainActivity = ViewModelProvider(this, factory).get(ViewModelMainActivity::class.java)








        workRequest = PeriodicWorkRequestBuilder<ConnectivityCheckWorker>(1, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(workRequest)



        binding.singUpbutton.setOnClickListener{
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)


        }

        binding.logInButton.setOnClickListener {
            val intentLogIN = Intent(this, LogInActivity::class.java)
            startActivity(intentLogIN)
        }



    }
}