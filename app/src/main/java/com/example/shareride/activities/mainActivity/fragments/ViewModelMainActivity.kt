package com.example.shareride.activities.mainActivity.fragments

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient

class viewModelMainActivity  : ViewModel()    {

    var page_name: String = "Home"


    var latitud_me_i:Double = 0.0
    var longitud_me_i:Double =0.0


    private val _isSwitchChecked = MutableLiveData<Boolean>()
    val isSwitchChecked: LiveData<Boolean>
        get() = _isSwitchChecked

    fun change_pg_name(newpage:String){
        page_name = newpage

    }



    // Function to update the LiveData
    fun setSwitchChecked(isChecked: Boolean) {
        _isSwitchChecked.value = isChecked
    }

    fun isswitchcheked(): MutableLiveData<Boolean> {
        return _isSwitchChecked
    }



    fun set_my_location(latitud:Double, logitud:Double){
        latitud_me_i = latitud
        longitud_me_i = logitud
    }




}