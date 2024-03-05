package com.example.shareride

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.shareride.activities.singUp.SingUpActivity
class StartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val singUpButton: Button = findViewById(R.id.singUpbutton)
        //val loginInButton: Button = findViewById(R.id.logInbutton)

        singUpButton.setOnClickListener{
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)


        }




    }
}