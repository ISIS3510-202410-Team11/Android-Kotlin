package com.example.shareride.activities.mainActivity

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
import com.example.shareride.activities.mainActivity.fragments.viewModelMainActivity
import com.example.shareride.databinding.ActivityMainBinding

class MainActivityPassenger : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    private lateinit var switch_role: SwitchCompat
    private lateinit var page_name: TextView
    var isSwitchOn: Boolean = true
    private var profileFragment: ProfilePassengerFragment? = null


    val viewModel: viewModelMainActivity by lazy {

        ViewModelProvider(this).get(viewModelMainActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(getInitialFragment())



        switch_role = binding.switchProfileType
        page_name = binding.pageName
        page_name.text = viewModel.page_name


        switch_role.setOnCheckedChangeListener{_, isChecked ->

            viewModel.setSwitchChecked(isChecked)
            isSwitchOn = isChecked

            when(viewModel.page_name){

                "Home"-> replaceFragment(getHomeFragment())
                "Profile"->replaceFragment(getProfileFragment())

            }


        }

        binding.bottomNavView.setOnItemSelectedListener {


            when(it.itemId ){
                R.id.home_it -> {replaceFragment(getHomeFragment())
                    viewModel.change_pg_name("Home")
                    page_name.text = viewModel.page_name

                }
                R.id.account_it -> {replaceFragment(getProfileFragment())
                    viewModel.change_pg_name("Profile")
                    page_name.text = viewModel.page_name
                }

                else->{


                }
                }
            true
        }


    }


    private fun getInitialFragment(): Fragment {
        return if(isSwitchOn){
            HomePassengerFragment()

        }
        else{
            //TODO HomeDriverFragment()
            HomePassengerFragment()
        }
    }


    private fun getHomeFragment():Fragment{

        return if(isSwitchOn){
            HomePassengerFragment()
        }
        else{
            //TODO HomeDriverFragment()

            HomePassengerFragment()
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



}