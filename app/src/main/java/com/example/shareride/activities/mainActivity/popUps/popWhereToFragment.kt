package com.example.shareride.activities.mainActivity.popUps

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.R
import com.example.shareride.activities.mainActivity.fragments.viewModelMainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.checkerframework.common.subtyping.qual.Bottom
import java.util.regex.Pattern


class popWhereToFragment : DialogFragment() {
    // TODO: Rename and change types of parameters



    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var my_location: String = ""
    var name_new_location: String = ""
    //private var viewModel: viewModelMainActivity = ViewModelProvider(requireActivity()).get(viewModelMainActivity::class.java)
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
        val botton_newLoc = view.findViewById<Button>(R.id.add_new_loc)

        val add_loc = view.findViewById<LinearLayout>(R.id.add_loc_linear)
        val hist_layour = view.findViewById<LinearLayout>(R.id.historical_whereto)

        val snd_button_ = view.findViewById<Button>(R.id.send_button)
        val txt_bar = view.findViewById<EditText>(R.id.new_loc_text)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        from_txt_bar.setOnClickListener {



            getLocation()

            from_txt_bar.textSize = 15F
            from_txt_bar.setText(my_location)





        }

        botton_newLoc.setOnClickListener {
            add_loc.visibility = View.VISIBLE
            hist_layour.visibility = View.GONE

        }

        snd_button_.setOnClickListener {

            if (name_new_location != null || name_new_location.isEmpty()) {


                // guardar en base de datos
            }



        }
        txt_bar.addTextChangedListener {


            name_new_location = it.toString()
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
                Toast.makeText(requireContext(), "Succesfully adquiered location", Toast.LENGTH_SHORT).show()
                my_location = "Latitude "+it.latitude.toString()+" Longitude "+it.longitude
            }


        }



    }


   /** private fun set_my_location():String{
        return if (viewModel.longitud_me_i == null || viewModel.latitud_me_i == null){
             "Click in here to calculate your location"

        }
        else{
            "Location: Latitude: "+viewModel.latitud_me_i.toString()+" , Longitude: "+viewModel.longitud_me_i.toString()
        }
    }*/


}