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
import com.example.shareride.activities.mainActivity.fragments.ViewModelMainActivity
import com.example.shareride.activities.vehicleForm.VehicleFormActivity
import com.example.shareride.clases.Warnings
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.connectivity.NetworkConnectivityObserver
import com.example.shareride.databinding.ActivitySingUpBinding
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class SingUpActivity : AppCompatActivity() {


    lateinit var singUpbutton: Button

    lateinit var textBar_name: EditText
    lateinit var textbar_email: EditText
    lateinit var textbar_password: EditText

    lateinit var singupTitle: TextView

    lateinit var new_intent :Intent
    lateinit var offlinewarning: LinearLayout

    lateinit var box_driver : CheckBox

    lateinit var warningEmail: LinearLayout
    lateinit var warningPassword: LinearLayout
    lateinit var warningName: LinearLayout

    val email_regexPattern = "^[\\w.-]+@(uniandes\\.)+(edu\\.co)\$"
    val name_regexPattern = "^[A-Za-z]{2,16}( [A-Za-z]{2,16})?$"
    val password_regexPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"

    lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    lateinit var fireBaseAuth : FirebaseAuth
    private lateinit var binding: ActivitySingUpBinding

    private  lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: viewModelSignUp

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivitySingUpBinding.inflate(layoutInflater)

        setContentView(binding.root)


        singUpbutton = binding.singUpbutton

        textBar_name= binding.nametextbar
        textbar_email = binding.emailTextBar
        textbar_password = binding.passwordTextbar


        singupTitle= binding.titleSignup

        offlinewarning = binding.offlineSign


        warningName = binding.warningName
        warningEmail = binding.warningEmail
        warningPassword = binding.warningPassword

        fireBaseAuth =FirebaseAuth.getInstance()

        box_driver  = binding.driver
        networkConnectivityObserver = NetworkConnectivityObserver(applicationContext)
        viewModelFactory = ViewModelFactory(networkConnectivityObserver, this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(viewModelSignUp::class.java)
        displayInva(avalability = false)
        display_wait_loading(true)
        viewModel.loadUserData(){ user ->
            displayInva(avalability = true)
            display_wait_loading(false)

            binding.passwordTextbar.setText(user.password)

            textbar_email.setText(user.email)

            textBar_name.setText(user.name)

            viewModel.loadWarning() {
                    warnings ->
                warn_visivility(warnings)
            }

        }













        binding.cancelButton.setOnClickListener {

            new_intent= Intent(this, StartActivity::class.java)
            startActivity(new_intent)
            Firebase.analytics.logEvent("Close_sign_up", null)
        }

        binding.logInButton.setOnClickListener {

            new_intent = Intent(this, LogInActivity::class.java)
            startActivity(new_intent)
        }

        binding.singUpbutton.setOnClickListener {




            if (warningName.visibility == View.GONE && warningEmail.visibility== View.GONE && warningPassword.visibility == View.GONE ){



                if (viewModel.connectivityStatus.value.toString() =="Lost" || viewModel.connectivityStatus.value.toString() =="Unavailable" ){


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

                                new_intent = Intent(this, VehicleFormActivity::class.java)
                                startActivity(new_intent)}



                            else{
                                new_intent = Intent(this, MainActivityPassenger::class.java)
                                startActivity(new_intent)
                            }




                        }
                        else{
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


                    fireBaseAuth.createUserWithEmailAndPassword(viewModel.inputEmail.value.toString(),viewModel.inputPassword.value.toString()).addOnSuccessListener {


                        println(viewModel.inputEmail.value.toString())


                            if(box_driver.isChecked){

                                new_intent = Intent(this, VehicleFormActivity::class.java)
                                startActivity(new_intent)}



                            else{
                                new_intent = Intent(this, MainActivityPassenger::class.java)
                                startActivity(new_intent)
                            }

                        }.addOnFailureListener{

                            showCustomToast(this, "Check that your data is correct")

                            displayInva(avalability = true)

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

                                    new_intent = Intent(this, VehicleFormActivity::class.java)
                                    startActivity(new_intent)}



                                else{
                                    new_intent = Intent(this, MainActivityPassenger::class.java)
                                    startActivity(new_intent)
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

                val name_corr=check_name(inputText)

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

        val pattern = Pattern.compile(email_regexPattern)
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

        val pattern = Pattern.compile(name_regexPattern)

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
        val pattern = Pattern.compile(password_regexPattern)
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