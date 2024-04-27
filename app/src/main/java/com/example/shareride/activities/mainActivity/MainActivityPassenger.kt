package com.example.shareride.activities.mainActivity

import ConnectivityReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.activities.mainActivity.fragments.HomePassengerFragment
import com.example.shareride.activities.mainActivity.fragments.ProfilePassengerFragment
import com.example.shareride.R
import com.example.shareride.activities.mainActivity.fragments.HomeDriverFragment
import com.example.shareride.activities.mainActivity.fragments.ViewModelMainActivity
import com.example.shareride.databinding.ActivityMainBinding
import com.example.shareride.service.FormService

class MainActivityPassenger : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding




    private lateinit var switch_role: SwitchCompat
    private lateinit var page_name: TextView
    var isSwitchOn: Boolean = true
    private var profileFragment: ProfilePassengerFragment? = null


    val viewModel: ViewModelMainActivity by lazy {

        ViewModelProvider(this).get(ViewModelMainActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.setSwitchChecked(false)

        replaceFragment(getInitialFragment())

        var connectivityReceiver = ConnectivityReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, filter)



        page_name = binding.pageName
        page_name.text = viewModel.page_name




        binding.bottomNavView.setOnItemSelectedListener {


            when(it.itemId ){
                R.id.home_it -> {replaceFragment(getHomeFragment())
                    page_name.text = viewModel.page_name
                    viewModel.clicks_bf_createride("Home")

                }
                R.id.account_it -> {replaceFragment(getProfileFragment())
                    page_name.text = viewModel.page_name
                    viewModel.clicks_bf_createride("Profile")

                }

                else->{


                }
                }
            true
        }


    }


    private fun getInitialFragment(): Fragment {
        return if(viewModel._isSwitchChecked.value == false){
            viewModel.change_pg_name("Home")

            HomePassengerFragment()


        }
        else{
            //TODO HomeDriverFragment()
            viewModel.change_pg_name("Home")

            HomeDriverFragment()
        }
    }


    private fun getHomeFragment():Fragment{


        return if(viewModel._isSwitchChecked.value == false){
            viewModel.change_pg_name("Home")

            HomePassengerFragment()
        }
        else{
            //TODO HomeDriverFragment()
            viewModel.change_pg_name("Home")

            HomeDriverFragment()
        }
    }

    private fun getProfileFragment(): Fragment{
        if (profileFragment == null) {

            profileFragment = ProfilePassengerFragment.newInstance()
        }
        return profileFragment!!
    }

    private fun replaceFragment(fragment:Fragment){

        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)

        if (currentFragment?.javaClass != fragment.javaClass) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commitNow()
        }
    }
    fun getSharedViewModel(): ViewModelMainActivity  {
        return viewModel
    }



}