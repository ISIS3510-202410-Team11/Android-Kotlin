package com.example.shareride.activities.logIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.example.shareride.R
import com.example.shareride.StartActivity
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.activities.mainActivity.MainActivityPassenger
import com.example.shareride.activities.forgotPasswordActivity.ForgotPasswordActivity
import com.example.shareride.activities.singUp.ViewModelFactory
import com.example.shareride.activities.singUp.viewModelSignUp
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import com.google.firebase.auth.FirebaseAuth


import com.example.shareride.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val closeButton: ImageButton = findViewById(R.id.cancel_button)
        closeButton.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

        binding.textView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        binding.singUpbutton2.setOnClickListener {
            val email = binding.emailInputSingUp.text.toString()
            val pass = binding.passwordInputSingUp.text.toString()

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
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onStart() {
        super.onStart()







        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivityPassenger::class.java)
            startActivity(intent)
        }
    }
}