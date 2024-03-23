package com.example.shareride.activities.mainActivity.popUps

import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.text.TextWatcher
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
import com.example.shareride.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.common.MapboxOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style

class popWhereToFragment : DialogFragment() {
    // TODO: Rename and change types of parameters

    private  lateinit var mapview:MapView



    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var my_location: String = ""
    var name_new_location: String = ""


    //private var viewModel: viewModelMainActivity = ViewModelProvider(requireActivity()).get(viewModelMainActivity::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapboxOptions.accessToken = "pk.eyJ1IjoicGNhbXBpbm9ycnIiLCJhIjoiY2x0eW8zY2Y0MGo3dzJocGFrNGxwZG0xNiJ9._UAVbyNYayAoA5RSiwOrfg"

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_where_to, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        mapview = MapView(this.requireContext())


        mapview.mapboxMap.setCamera(CameraOptions.Builder()
            .center(com.mapbox.geojson.Point.fromLngLat(-98.0, 39.5)).pitch(0.0).zoom(2.0)
            .bearing(0.0)
            .build()
        )
        mapview.mapboxMap.loadStyle(Style.MAPBOX_STREETS)










        super.onViewCreated(view, savedInstanceState)

        val from_txt_bar = view.findViewById<EditText>(R.id.fromTxT)
        val botton_newLoc = view.findViewById<Button>(R.id.add_new_loc)

        val add_loc = view.findViewById<LinearLayout>(R.id.add_loc_linear)
        val hist_layour = view.findViewById<LinearLayout>(R.id.historical_whereto)

        val snd_button_ = view.findViewById<Button>(R.id.send_button)
        val txt_bar = view.findViewById<EditText>(R.id.new_loc_text)


        val map_view = view.findViewById<MapView>(R.id.mapView)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        from_txt_bar.setOnClickListener {



            getLocation()

            from_txt_bar.textSize = 15F
            from_txt_bar.setText(my_location)





        }

        botton_newLoc.setOnClickListener {
            add_loc.visibility = View.VISIBLE
            hist_layour.visibility = View.GONE
            snd_button_.visibility = View.GONE






        }





        snd_button_.setOnClickListener {

            if (name_new_location != null || name_new_location.isEmpty()) {


                // guardar en base de datos
            }
            else{
                txt_bar.setText("")

                Toast.makeText(requireContext(), "Check your information", Toast.LENGTH_SHORT).show()

            }



        }





    }

    fun getLocation (){
        if(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.INTERNET)!=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.INTERNET
                ),100)
            return
        }

        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {


            if(it!= null){
                Toast.makeText(requireContext(), "Succesfully adquiered location", Toast.LENGTH_SHORT).show()
                my_location = "Latitude "+it.latitude.toString()+" Longitude "+it.longitude
            }
            else{
                Toast.makeText(requireContext(), "Unsucesfull adquiered location", Toast.LENGTH_SHORT).show()

            }


        }



    }



}