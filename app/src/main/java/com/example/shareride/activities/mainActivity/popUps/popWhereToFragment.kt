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
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shareride.R
import com.example.shareride.activities.mainActivity.fragments.ViewModelMainActivity
import com.example.shareride.activities.trips.TripActivity
import com.example.shareride.connectivity.ConnectivityObserver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.MapboxOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location


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

    private  lateinit var noInternet: LinearLayout
    private  lateinit var nomap: LinearLayout

    private  lateinit var to_txt_bar: EditText
    private  lateinit var from_txt_bar: EditText


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var my_location: String = ""
    var name_new_location: String = ""
    var my_latitud: Double =0.0
    var my_longitud:Double =0.0



    private  val viewModel: ViewModelMainActivity by activityViewModels()

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

    override fun onStart() {
        super.onStart()
        mapview.onStart()

        to_txt_bar.setText(viewModel.destination.value.toString())

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
            viewModel.clicks_bf_createride("map")
            if (viewModel.connectivityStatus.value.toString() == "Avalilable" || viewModel.connectivityStatus.value.toString() == "Losing"){

                viewModel.reverse_geocode_destination(longitude, latitude)}
            else{
                viewModel.destination.value="We need you to be connected to acces your location"
            }
            true

        }
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapview.onDestroy()
    }



    override fun onLowMemory() {
        super.onLowMemory()
        mapview.onLowMemory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        noInternet = view.findViewById<LinearLayout>(R.id.offlineSign)
        val search_button = view.findViewById<Button>(R.id.ssearchbutton)
        nomap = view.findViewById(R.id.NoIntenernet)
         from_txt_bar = view.findViewById<EditText>(R.id.fromTxT)
         to_txt_bar = view.findViewById<EditText>(R.id.ToTxT)
        val botton_newLoc = view.findViewById<Button>(R.id.add_new_loc)

        val add_loc = view.findViewById<LinearLayout>(R.id.add_loc_linear)

        val snd_button_ = view.findViewById<Button>(R.id.send_button)
        val txt_bar = view.findViewById<EditText>(R.id.new_loc_text)


        viewModel.connectivityStatus.observe(this){status ->
            println(status.toString())
            when (status) {

                 ConnectivityObserver.Status.Unavailable ,  ConnectivityObserver.Status.Lost-> {

                     noInternet.visibility = View.VISIBLE
                     search_button.isEnabled = true
                     //search_button.isClickable = false
                     //search_button.alpha = 0.5f
                     add_loc.visibility = View.GONE






                }


                ConnectivityObserver.Status.Avalilable, ConnectivityObserver.Status.Losing -> {
                    noInternet.visibility = View.GONE
                    search_button.isEnabled = true
                    search_button.isClickable = true
                    search_button.alpha = 1f
                    nomap.visibility =View.GONE


                }



                else -> {}

            }

        }



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
            viewModel.clicks_bf_createride("map")
            viewModel.reverse_geocode_destination(longitude, latitude)
            true
        }






        from_txt_bar.addTextChangedListener (object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.origin.value = s.toString()
                println(viewModel.origin.value )
            }

            override fun afterTextChanged(s: Editable?) {

            }
        } )

        to_txt_bar.addTextChangedListener (object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.destination.value = s.toString()
                println(viewModel.destination.value )

            }

            override fun afterTextChanged(s: Editable?) {


            }} )


        from_txt_bar.setOnClickListener {

            viewModel.clicks_bf_createride("from bar")

            if (viewModel.connectivityStatus.value.toString() == "Avalilable" || viewModel.connectivityStatus.value.toString() == "Losing"){



                saveLocation()

            from_txt_bar.textSize = 15F

            myViewModel.livelatitud.observe(this, Observer { latitude ->
                myViewModel.livelaongitud.value?.let { longitude ->
                        viewModel.reverse_geocode(longitude,latitude )


                }
            })}
            else{
                from_txt_bar.hint = " We can't access your location"
            }




        }


        botton_newLoc.setOnClickListener {
            viewModel.clicks_bf_createride("new loc")


            if (viewModel.connectivityStatus.value.toString() == "Avalilable" || viewModel.connectivityStatus.value.toString() == "Losing"){

                if(!viewModel.isaddingloc){

                    viewModel.isaddingloc = true
                    snd_button_.visibility = View.VISIBLE
                    add_loc.visibility = View.VISIBLE
                    nomap.visibility = View.GONE
                    mapview.visibility = View.VISIBLE
                    mapview.onStart()


                }
                else{
                    viewModel.isaddingloc = false

                    snd_button_.visibility = View.GONE

                    add_loc.visibility = View.GONE
                    nomap.visibility = View.GONE
                    println("destrucción")
                }

            }
            else{
                if(!viewModel.isaddingloc){

                    viewModel.isaddingloc = true
                    snd_button_.visibility = View.VISIBLE
                    nomap.visibility = View.VISIBLE
                    mapview.onDestroy()
                }
                else{
                    viewModel.isaddingloc = false

                    snd_button_.visibility = View.GONE
                    nomap.visibility = View.GONE
                    mapview.onDestroy()

                }

            }









        }






        search_button.setOnClickListener {

            viewModel.clicks_bf_createride("search")






                if (viewModel.destination.value != null || viewModel.origin.value != null){
                   println(viewModel.origin.value)
                    val intent = Intent(requireContext(), TripActivity::class.java).apply {
                        putExtra("DESTINATION", viewModel.destination.value.toString()?: "")
                        putExtra("ORIGIN", viewModel.origin.value.toString() ?: "")

                    }
                    startActivity(intent)




                }
            else{

                    Toast.makeText(requireContext(), "Check your data", Toast.LENGTH_SHORT).show()

                }



        }








        snd_button_.setOnClickListener {

            viewModel.clicks_bf_createride("send button")


            if (myViewModel.name_place.value != null || myViewModel.name_place.value!="" ) {


                // TODO:(Enviar nuevo lugar)
                add_loc.visibility = View.GONE


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
                Toast.makeText(requireContext(), "Unsucesfull adquiered location, please turn on your Location", Toast.LENGTH_SHORT).show()

            }


        }






    }


}





