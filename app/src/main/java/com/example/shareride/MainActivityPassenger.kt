package com.example.shareride

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.shareride.databinding.ActivityMainBinding

class MainActivityPassenger : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomePassengerFragment())
        binding.bottomNavView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home_it -> replaceFragment(HomePassengerFragment())
                R.id.account_it -> replaceFragment(ProfilePassengerFragment())

                else->{


                }
                }
            true
        }
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