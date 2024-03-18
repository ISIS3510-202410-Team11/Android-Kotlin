package com.example.shareride

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.shareride.activities.mainActivity.MainActivityPassenger
import com.example.shareride.activities.singUp.SingUpActivity
import com.example.shareride.activities.logIn.logInActivity
class StartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val singUpButton: Button = findViewById(R.id.singUpbutton)
<<<<<<< HEAD
        val loginInButton: Button = findViewById(R.id.logInbutton)
=======
        val loginInButton: Button = findViewById(R.id.logInButton)
>>>>>>> 821c9b64a48aff25c0b0a1be937601e37f5f1ead

        singUpButton.setOnClickListener{
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)
        }

<<<<<<< HEAD
        loginInButton.setOnClickListener{
            val intent = Intent(this, logInActivity::class.java)
            startActivity(intent)
        }
=======
        loginInButton.setOnClickListener {
            val intent_t = Intent(this, MainActivityPassenger::class.java)
            startActivity(intent_t)
        }


>>>>>>> 821c9b64a48aff25c0b0a1be937601e37f5f1ead

    }
}