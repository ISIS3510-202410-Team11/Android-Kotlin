package com.example.shareride.activities.mainActivity.popUps

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.Manifest
import com.example.shareride.R
import com.example.shareride.activities.mainActivity.fragments.viewModelMainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class popWhereToFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var viewModel: viewModelMainActivity =
        ViewModelProvider(requireActivity()).get(viewModelMainActivity::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_where_to, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {




        super.onViewCreated(view, savedInstanceState)

        val from_txt_bar = view.findViewById<EditText>(R.id.fromTxT)
        from_txt_bar.setText(set_my_location())

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        from_txt_bar.setOnClickListener {

            getLocation()


            from_txt_bar.setText(set_my_location())



        }


    }

    private fun getLocation (){
        if(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),100)
            return
        }

        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {

            if(it!= null){
                viewModel.set_my_location(it.latitude, it.longitude)

            }
        }



    }

    private fun set_my_location():String{
        return if (!viewModel.latitud_me && !viewModel.longitud_me){
             "Click in here to calculate your location"

        }
        else{
            "Location: Latitude: "+viewModel.latitud_me.toString()+" , Longitude: "+viewModel.longitud_me.toString()
        }
    }


}