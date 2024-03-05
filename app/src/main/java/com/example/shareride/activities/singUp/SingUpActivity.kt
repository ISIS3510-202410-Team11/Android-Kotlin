package com.example.shareride.activities.singUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toolbar
import androidx.activity.compose.setContent
import com.example.shareride.R
import com.example.shareride.activities.StartActivity.StartActivity
import com.example.shareride.activities.logIn.logInActivity

class SingUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        val closeButton: ImageButton = findViewById(R.id.cancel_button)

        closeButton.setOnClickListener {

            val intent = Intent(this,StartActivity::class.java)
            startActivity(intent)
        }


}
}