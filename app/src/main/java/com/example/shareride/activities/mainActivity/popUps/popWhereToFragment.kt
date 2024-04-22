package com.example.shareride.activities.mainActivity.popUps

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.shareride.MapBoxAPI.IQLocationAPI
import com.example.shareride.R
import com.example.shareride.activities.mainActivity.fragments.ViewModelMainActivity
import com.example.shareride.activities.singUp.SingUpActivity
import com.example.shareride.activities.trips.TripActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.launch


class MyViewModel : ViewModel() {
    val livelatitud: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }

    val livelaongitud: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }

    val name_place: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val livelatitudDestination: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }

    val livelaongitudDestination: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }


}
class popWhereToFragment : DialogFragment() {
    // TODO: Rename and change types of parameters

    private  lateinit var mapview:MapView

    private lateinit var myViewModel: MyViewModel

    private  lateinit var progreess: ProgressBar
    private  lateinit var mlocIcon: ImageButton
    private  lateinit var progressDestination: ProgressBar
    private  lateinit var destinationIcon: ImageButton




    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var my_location: String = ""
    var name_new_location: String = ""
    var my_latitud: Double =0.0
    var my_longitud:Double =0.0



    private  val viewModel: ViewModelMainActivity by activityViewModels()

    //private var viewModel: viewModelMainActivity = ViewModelProvider(requireActivity()).get(viewModelMainActivity::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)


        MapboxOptions.accessToken = "pk.eyJ1IjoicGNhbXBpbm9ycnIiLCJhIjoiY2x0eW8zY2Y0MGo3dzJocGFrNGxwZG0xNiJ9._UAVbyNYayAoA5RSiwOrfg"
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_where_to, container, false)
        saveLocation()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        mapview = view.findViewById<MapView>(R.id.mapView)
        var locationComponetn = mapview.location


        mapview.mapboxMap.loadStyle(Style.MAPBOX_STREETS)

        mapview.getMapboxMap().setCamera(CameraOptions.Builder().zoom(14.0).build())



                val positionList=OnIndicatorPositionChangedListener{

                    mapview.mapboxMap.setCamera(CameraOptions.Builder().center(it).build())

                    mapview.gestures.focalPoint= mapview.getMapboxMap().pixelForCoordinate(it)

                }

                locationComponetn.updateSettings {
                    this.enabled=true


                    locationComponetn.addOnIndicatorPositionChangedListener(positionList)
                }





         val onMoveListener = object:OnMoveListener{
             override fun onMove(detector: MoveGestureDetector): Boolean {
                 TODO("Not yet implemented")
             }

             override fun onMoveBegin(detector: MoveGestureDetector) {
                 TODO("Not yet implemented")
             }

             override fun onMoveEnd(detector: MoveGestureDetector) {
                 TODO("Not yet implemented")
             }

         }













        super.onViewCreated(view, savedInstanceState)

        val from_txt_bar = view.findViewById<EditText>(R.id.fromTxT)
        val to_txt_bar = view.findViewById<EditText>(R.id.ToTxT)
        val botton_newLoc = view.findViewById<Button>(R.id.add_new_loc)

        val add_loc = view.findViewById<LinearLayout>(R.id.add_loc_linear)
        val hist_layour = view.findViewById<LinearLayout>(R.id.historical_whereto)

        val snd_button_ = view.findViewById<Button>(R.id.send_button)
        val txt_bar = view.findViewById<EditText>(R.id.new_loc_text)





        val search_button = view.findViewById<Button>(R.id.ssearchbutton)

        progreess = view.findViewById(R.id.mylocationloading)
        mlocIcon= view.findViewById(R.id.mylocationicon)

        progressDestination = view.findViewById(R.id.mydestinationloading)
        destinationIcon = view.findViewById(R.id.iddestiantionicon)




        viewModel.isRequestPending.observe(this, Observer { isRequestPending ->
            if (isRequestPending) {
                progreess.visibility = View.VISIBLE
                mlocIcon.visibility = View.GONE

            } else {
                progreess.visibility = View.GONE
                mlocIcon.visibility =  View.VISIBLE
            }
        })




        viewModel.isRequestDestinationPending.observe(this, Observer { isRequestDestinationPending ->
            if (isRequestDestinationPending) {
                progressDestination.visibility = View.VISIBLE
                destinationIcon.visibility = View.GONE

            } else {
                progressDestination.visibility = View.GONE
                destinationIcon.visibility =  View.VISIBLE
            }
        })



        mapview.getMapboxMap().addOnMapClickListener { point ->


            val latitude = point.latitude()
            val longitude = point.longitude()
            val coordenadas = LatLng(latitude, longitude)
            val destination_unk = "Latitud: $latitude, Longitud: $longitude"
            to_txt_bar.hint = destination_unk
            to_txt_bar.setText(destination_unk)
            to_txt_bar.textSize = 12.0F
            myViewModel.livelaongitudDestination.postValue(longitude)
            myViewModel.livelatitudDestination.postValue(latitude)

            viewModel.reverse_geocode_destination(longitude, latitude)
        }


        viewModel.destination.observe(viewLifecycleOwner, Observer { newDestination ->
            to_txt_bar.setText(newDestination)
            to_txt_bar.setHint(newDestination)

        })



        viewModel.origin.observe(viewLifecycleOwner, Observer { newOrigin ->
            from_txt_bar.setText(newOrigin)
            from_txt_bar.setHint(newOrigin)

        })

        from_txt_bar.addTextChangedListener { object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.origin.value = s.toString()

            }
        } }

        to_txt_bar.addTextChangedListener { object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.destination.value = s.toString()

            }} }


        from_txt_bar.setOnClickListener {



            saveLocation()

            from_txt_bar.textSize = 15F

            myViewModel.livelatitud.observe(this, Observer { latitude ->
                myViewModel.livelaongitud.value?.let { longitude ->
                        viewModel.reverse_geocode(longitude,latitude )


                }
            })}


        botton_newLoc.setOnClickListener {

            if(!viewModel.isaddingloc){

                viewModel.isaddingloc = false
                hist_layour.visibility = View.GONE
                snd_button_.visibility = View.VISIBLE
                add_loc.visibility = View.VISIBLE}
            else{
                viewModel.isaddingloc = true

                hist_layour.visibility = View.VISIBLE
                snd_button_.visibility = View.GONE
                add_loc.visibility = View.GONE

            }






        }






        search_button.setOnClickListener {


            val intent = Intent(requireContext(), TripActivity::class.java)
            startActivity(intent)

        }








        snd_button_.setOnClickListener {

            if (myViewModel.name_place.value != null || myViewModel.name_place.value!="" ) {


                // TODO:(Enviar nuevo lugar)
                add_loc.visibility = View.GONE
                hist_layour.visibility = View.VISIBLE


            }
            else{
                txt_bar.setText("")

                Toast.makeText(requireContext(), "Check your information", Toast.LENGTH_SHORT).show()

            }



        }












    }

    fun updateLatitud(newValue: Double) {
        myViewModel.livelatitud.value = newValue
    }

    fun uupdatelongitud(newValue: Double) {
        myViewModel.livelaongitud.value = newValue
    }


    fun saveLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.INTERNET
                ),
                100
            )
            return
        }

        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {

            if(it!= null){
                Toast.makeText(requireContext(), "Succesfully adquiered location", Toast.LENGTH_SHORT).show()
                my_location = "Latitude "+it.latitude.toString()+" Longitude "+it.longitude



                updateLatitud(it.latitude)
                uupdatelongitud(it.longitude)

            }
            else{
                Toast.makeText(requireContext(), "Unsucesfull adquiered location", Toast.LENGTH_SHORT).show()

            }


        }






    }


}





