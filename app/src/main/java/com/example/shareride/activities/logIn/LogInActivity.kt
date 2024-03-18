package com.example.shareride.activities.logIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.shareride.R
import com.example.shareride.StartActivity

class logInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        val closeButton: ImageButton = findViewById(R.id.cancel_button)
        closeButton.setOnClickListener {

            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
    }

}