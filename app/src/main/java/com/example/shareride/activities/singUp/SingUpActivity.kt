package com.example.shareride.activities.singUp

import android.content.Context
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.activities.mainActivity.MainActivityPassenger
import com.example.shareride.R
import com.example.shareride.StartActivity
import com.example.shareride.activities.logIn.LogInActivity
import com.example.shareride.activities.vehicleForm.VehicleFormActivity
import com.example.shareride.clases.Warnings
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class SingUpActivity : AppCompatActivity() {


    lateinit var closeButton: ImageButton
    lateinit var singUpbutton: Button
    lateinit var logInButton: TextView

    lateinit var textBar_name: EditText
    lateinit var textbar_email: EditText
    lateinit var textbar_password: EditText

    lateinit var singupTitle: TextView


    lateinit var offlinewarning: LinearLayout



    lateinit var warningEmail: LinearLayout
    lateinit var warningPassword: LinearLayout
    lateinit var warningName: LinearLayout

    var match_mail: Boolean= false
    var match_pass: Boolean= false
    var match_name: Boolean= false
    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        closeButton = findViewById(R.id.cancel_button)
        singUpbutton = findViewById(R.id.singUpbutton)
        logInButton = findViewById(R.id.logInButton)

        textBar_name= findViewById(R.id.nametextbar)
        textbar_email = findViewById(R.id.emailTextBar)
        textbar_password = findViewById(R.id.passwordTextbar)
        singupTitle= findViewById(R.id.title_signup)

        offlinewarning = findViewById(R.id.offlineSign)


        warningName = findViewById(R.id.warning_name)
        warningEmail = findViewById(R.id.warning_email)
        warningPassword = findViewById(R.id.warning_password)


        val networkConnectivityObserver = NetworkConnectivityObserver(applicationContext)
        val viewModelFactory = ViewModelFactory(networkConnectivityObserver, this)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(viewModelSignUp::class.java)
        displayInva(avalability = false)
        display_wait_loading(true)
        viewModel.loadUserData(){ user ->
            displayInva(avalability = true)
            display_wait_loading(false)

            textbar_password.setText(user.password)

            textbar_email.setText(user.email)

            textBar_name.setText(user.name)

            viewModel.loadWarning() {
                    warnings ->
                warn_visivility(warnings)
            }

        }












        val fireBaseAuth =FirebaseAuth.getInstance()

        val box_driver : CheckBox = findViewById(R.id.driver)
        closeButton.setOnClickListener {

            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            Firebase.analytics.logEvent("Close_sign_up", null)
        }

        logInButton.setOnClickListener {

            val logInAcc = Intent(this, LogInActivity::class.java)
            startActivity(logInAcc)
        }

        singUpbutton.setOnClickListener {




            if (warningName.visibility == View.GONE && warningEmail.visibility== View.GONE && warningPassword.visibility == View.GONE ){


                println("vew value")
                println(viewModel.connectivityStatus.value.toString() )

                if (viewModel.connectivityStatus.value.toString() =="Lost" || viewModel.connectivityStatus.value.toString() =="Unavailable" ){

                    println(viewModel.connectivityStatus.value.toString() )

                    if (viewModel.pending_singup){


                        showCustomToast(this, "Your user will be created after your recover connectivity")

                    }else{
                        viewModel.pending_singup = true
                        displayInva(avalability = false)
                        showCustomToast(this, "We will notify the creation of your user once you recover connectivity")

                    }


                }
                else{

                    println(viewModel.inputEmail.value.toString())
                    println(viewModel.inputPassword.value.toString())
                    fireBaseAuth.createUserWithEmailAndPassword(viewModel.inputEmail.value.toString(),viewModel.inputPassword.value.toString()).addOnCompleteListener {
                        if(it.isSuccessful){
                            if(box_driver.isChecked){

                                val intent = Intent(this, VehicleFormActivity::class.java)
                                startActivity(intent)}



                            else{
                                val intent = Intent(this, MainActivityPassenger::class.java)
                                startActivity(intent)
                            }




                        }
                        else{
                            // si el email está repetido manda un error
                            showCustomToast(this, "Verify your email and password")

                        }

                    }
                }

            }

            else{
                showCustomToast(this, "Check that your data is correct")

            }

        }

        textbar_password.addTextChangedListener (object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password_new= s.toString()
                val correctnes=check_password(password_new)

                viewModel.change_password(password_new,correctnes)

            }

            override fun afterTextChanged(s: Editable?) {



            }


        })




        textbar_email.addTextChangedListener  (object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputEmail = s.toString()
                val correctens=check_email(inputEmail)
                viewModel.change_email(inputEmail,correctens)

            }

            override fun afterTextChanged(s: Editable?) {


            }


        })

        viewModel.connectivityStatus.observe(this@SingUpActivity) { status ->
            when (status) {


                ConnectivityObserver.Status.Lost -> {
                offlinewarning.visibility = View.VISIBLE

            }

                ConnectivityObserver.Status.Unavailable -> {
                    offlinewarning.visibility = View.VISIBLE

                }
                ConnectivityObserver.Status.Avalilable ->{
                offlinewarning.visibility = View.GONE
                if (viewModel.pending_singup){
                    displayInva(avalability = false)


                    fireBaseAuth.createUserWithEmailAndPassword(viewModel.inputEmail.value.toString(),viewModel.inputPassword.value.toString()).addOnCompleteListener {


                        if(it.isSuccessful){

                            if(box_driver.isChecked){

                                val intent = Intent(this, VehicleFormActivity::class.java)
                                startActivity(intent)}



                            else{
                                val intent = Intent(this, MainActivityPassenger::class.java)
                                startActivity(intent)
                            }

                        }
                        else{

                            println(it.result)
                            showCustomToast(this, "Check that your data is correct")

                            displayInva(avalability = true)

                        }
                    }

                }

            }

                ConnectivityObserver.Status.Losing ->{
                    offlinewarning.visibility = View.GONE
                    if (viewModel.pending_singup){
                        displayInva(avalability = false)

                        fireBaseAuth.createUserWithEmailAndPassword(viewModel.inputEmail.value.toString(),viewModel.inputPassword.value.toString()).addOnCompleteListener {


                            if(it.isSuccessful){

                                if(box_driver.isChecked){

                                    val intent = Intent(this, VehicleFormActivity::class.java)
                                    startActivity(intent)}



                                else{
                                    val intent = Intent(this, MainActivityPassenger::class.java)
                                    startActivity(intent)
                                }

                            }
                            else{
                                showCustomToast(this, "Check that your data is correct")
                                displayInva(avalability = true)

                            }
                        }

                    }

                }

                else -> {}
            }
        }

        textBar_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                 val inputText = s.toString()

                val name_corr=check_name(viewModel.inputText.value.toString())

                viewModel.change_name(inputText,name_corr)

            }

            override fun afterTextChanged(s: Editable?) {


            }
        })



}

    fun showCustomToast(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setCancelable(true)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()

        val textViewMessage = alert.findViewById<TextView>(android.R.id.message)
        textViewMessage?.textSize = 18f
    }

    fun displayInva( avalability: Boolean){

        textbar_password.isEnabled = avalability
        textbar_email.isEnabled = avalability
        textBar_name.isEnabled= avalability
        singUpbutton.isEnabled = avalability
        singUpbutton.isClickable = avalability
        if (!avalability ){
            singUpbutton.text = "Loading .."
        }
        else{
            singUpbutton.text = "Sing Up"

        }


    }
    fun check_email(email:String): Boolean {

        val regexPattern = "^[\\w.-]+@(uniandes\\.)+(edu\\.co)\$"
        val pattern = Pattern.compile(regexPattern)
        val matcher = pattern.matcher(email)

        if (!matcher.matches()) {
            warningEmail.visibility= View.VISIBLE
            return true
        }
        else{
            warningEmail.visibility= View.GONE
            return false

        }

    }
    fun check_name(name:String): Boolean {
        val regexPattern = "^[A-Za-z]{2,16}( [A-Za-z]{2,16})?$"

        val pattern = Pattern.compile(regexPattern)

        val matcher = pattern.matcher(name)

        if (!matcher.matches()) {
            warningName.visibility= View.VISIBLE
            return true
        }
        else{
            warningName.visibility= View.GONE
            return false

        }
    }

    fun check_password(password:String): Boolean {
        val regexPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"
        val pattern = Pattern.compile(regexPattern)
        val matcher = pattern.matcher(password)

        if (!matcher.matches()) {
            warningPassword.visibility= View.VISIBLE
            return true
        }
        else{
            warningPassword.visibility= View.GONE
            return false

        }
    }


    fun warn_visivility(warnings: Warnings){

        if (warnings.match_pass) {
            warningPassword.visibility= View.VISIBLE
        }
        else{
            warningPassword.visibility= View.GONE

        }

        if (warnings.match_name) {
            warningName.visibility= View.VISIBLE
        }
        else{
            warningName.visibility= View.GONE

        }

        if (warnings.match_mail) {
            warningEmail.visibility= View.VISIBLE
        }
        else{
            warningEmail.visibility= View.GONE

        }
    }

    fun display_wait_loading(up:Boolean){
        if (up){

            singupTitle.text="Sign Up: loading"
            singupTitle.textSize=15.0F

        }
        else{
            singupTitle.text="Sign Up"
            singupTitle.textSize=35.0F

        }


    }
}