package com.example.shareride

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.shareride.activities.singUp.SingUpActivity
import com.example.shareride.activities.logIn.LogInActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shareride.clases.ConnectivityCheckWorker
import java.util.concurrent.TimeUnit

class StartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
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