package com.example.shareride.activities.StartActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.compose.ui.tooling.preview.Preview
import com.example.shareride.R
import com.example.shareride.activities.singUp.SingUpActivity
class StartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val singUpButton: Button = findViewById(R.id.singUpbutton)
        val loginInButton: Button = findViewById(R.id.logInbutton)

        singUpButton.setOnClickListener{
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)


        }




    }
}