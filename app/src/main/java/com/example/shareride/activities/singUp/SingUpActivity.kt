package com.example.shareride.activities.singUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.activities.mainActivity.MainActivityPassenger
import com.example.shareride.R
import com.example.shareride.StartActivity
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class SingUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        val closeButton: ImageButton = findViewById(R.id.cancel_button)
        val singUpbutton: Button = findViewById(R.id.singUpbutton)

        val textBar_name: EditText = findViewById(R.id.nametextbar)
        val textbar_email: EditText = findViewById(R.id.emailTextBar)
        val textbar_password: EditText = findViewById(R.id.passwordTextbar)


        val viewModel = ViewModelProvider(this).get(viewModelSignUp::class.java)


        var inputpassword: String= viewModel.inputpassword
        var inputEmail: String = viewModel.inputEmail
        var inputText: String= viewModel.inputText


        val warningName: LinearLayout = findViewById(R.id.warning_name)
        val warningEmail: LinearLayout = findViewById(R.id.warning_email)
        val warningPassword: LinearLayout = findViewById(R.id.warning_password)


        val fireBaseAuth =FirebaseAuth.getInstance()

        val box_driver : CheckBox = findViewById(R.id.driver)
        closeButton.setOnClickListener {

            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            Firebase.analytics.logEvent("Close_sign_up", null)
        }

        singUpbutton.setOnClickListener {

            if (warningName.visibility == View.GONE && warningEmail.visibility== View.GONE && warningPassword.visibility == View.GONE && !box_driver.isChecked){





                fireBaseAuth.createUserWithEmailAndPassword(viewModel.inputEmail,viewModel.inputpassword).addOnCompleteListener {


                    if(it.isSuccessful){

                     val intent = Intent(this, MainActivityPassenger::class.java)
                     startActivity(intent)

                 }
                 else{
                     // si el email está repetido manda un error
                     Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()

                 }
                }
            }
            else if(warningName.visibility == View.GONE && warningEmail.visibility== View.GONE && warningPassword.visibility == View.GONE && box_driver.isChecked){
                //TODO:"Redirects to vehicle form"


            }
            else{
                Toast.makeText(this, "Check that everything is correct", Toast.LENGTH_SHORT).show()

            }

        }

        textbar_password.addTextChangedListener (object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                 inputpassword = s.toString()
                val regexPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"
                val pattern = Pattern.compile(regexPattern)
                val matcher = pattern.matcher(inputpassword)
                viewModel.change_password(inputpassword)
                if (!matcher.matches()) {
                    warningPassword.visibility= View.VISIBLE
                }
                else{
                    warningPassword.visibility= View.GONE

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }


        })


        textbar_email.addTextChangedListener  (object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                 inputEmail = s.toString()
                val regexPattern = "^[\\w.-]+@(uniandes\\.)+(edu\\.co)\$"
                val pattern = Pattern.compile(regexPattern)
                val matcher = pattern.matcher(inputEmail)
                viewModel.change_email(inputEmail)

                if (!matcher.matches()) {
                    warningEmail.visibility= View.VISIBLE
                }
                else{
                    warningEmail.visibility= View.GONE

                }
            }

            override fun afterTextChanged(s: Editable?) {


            }


        })

        textBar_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                 inputText = s.toString()

                val regexPattern = "^[A-Za-z]{2,16}( [A-Za-z]{2,16})?$"

                val pattern = Pattern.compile(regexPattern)
                viewModel.change_name(inputText)

                val matcher = pattern.matcher(inputText)
                if (!matcher.matches()) {
                    warningName.visibility= View.VISIBLE
                }
                else{
                    warningName.visibility= View.GONE

                }
            }

            override fun afterTextChanged(s: Editable?) {


            }
        })


}
}