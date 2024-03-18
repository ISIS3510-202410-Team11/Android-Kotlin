package com.example.shareride.activities.mainActivity.fragments

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient

class viewModelMainActivity  : ViewModel()    {

    var page_name: String = "Home"

    private val latitud_me_i = MutableLiveData<Double>()
    var latitud_me: LiveData<Double> = TODO()
        get() = latitud_me_i
    private val longitud_me_i = MutableLiveData<Double>()
    var longitud_me: LiveData<Double> = TODO()
        get() = longitud_me_i


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
        latitud_me_i.value = latitud
        longitud_me_i.value = logitud
    }




}