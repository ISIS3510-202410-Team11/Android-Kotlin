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
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.shareride.MainActivityDriver
import com.example.shareride.R
import com.example.shareride.StartActivity
import java.util.regex.Pattern


class SingUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        val closeButton: ImageButton = findViewById(R.id.cancel_button)
        val singUpbutton: Button = findViewById(R.id.singUpbutton)

        val textBar_name: EditText = findViewById(R.id.nametextbar)
        val textbar_email: EditText = findViewById(R.id.emailTextBar)
        val  textbar_password: EditText = findViewById(R.id.passwordTextbar)


        val warningName: LinearLayout = findViewById(R.id.warning_name)
        val warningEmail: LinearLayout = findViewById(R.id.warning_email)
        val warningPassword: LinearLayout = findViewById(R.id.warning_password)

        val box_driver : CheckBox = findViewById(R.id.driver)
        closeButton.setOnClickListener {

            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

        singUpbutton.setOnClickListener {

            if (warningName.visibility == View.GONE && warningEmail.visibility== View.GONE && warningPassword.visibility == View.GONE && !box_driver.isChecked){
                val intent = Intent(this, MainActivityDriver::class.java)
                startActivity(intent)
                //TODO:"Pasarlo a base de datos"
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
                val inputpassword = s.toString()
                val regexPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"
                val pattern = Pattern.compile(regexPattern)
                val matcher = pattern.matcher(inputpassword)
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
                val inputEmail = s.toString()
                val regexPattern = "^[\\w.-]+@(uniandes\\.)+(edu\\.co)\$"
                val pattern = Pattern.compile(regexPattern)
                val matcher = pattern.matcher(inputEmail)
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
                val inputText = s.toString()

                val regexPattern = "^[A-Za-z]{2,16}( [A-Za-z]{2,16})?$"

                val pattern = Pattern.compile(regexPattern)

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