package com.example.shareride.activities.singUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.example.shareride.MainActivityDriver
import com.example.shareride.R
import com.example.shareride.StartActivity


class SingUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        val closeButton: ImageButton = findViewById(R.id.cancel_button)
        val singUpbutton: Button = findViewById(R.id.singUpbutton)

        closeButton.setOnClickListener {

            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

        singUpbutton.setOnClickListener {
            val intent = Intent(this, MainActivityDriver::class.java)
            startActivity(intent)
        }


}
}