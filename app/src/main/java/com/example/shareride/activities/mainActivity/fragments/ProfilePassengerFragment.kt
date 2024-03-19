package com.example.shareride.activities.mainActivity.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R
import com.example.shareride.StartActivity
import com.example.shareride.activities.custom_cards.CustomAdapterCard
import com.example.shareride.activities.custom_cards.CustomVehicleCard
import com.example.shareride.clases.Transport


class ProfilePassengerFragment : Fragment() {



    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_passenger, container, false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val recylceViewCars = view.findViewById<RecyclerView>(R.id.carsRecycleView)
        val add_car_button = view.findViewById<Button>(R.id.add_transportation)

        val viewModel: viewModelMainActivity =
            ViewModelProvider(requireActivity()).get(viewModelMainActivity::class.java)




        val name : TextView = view.findViewById(R.id.me_label)
        val rating: TextView = view.findViewById(R.id.me_rating)
        val method_p : TextView = view.findViewById(R.id.me_method)
        val button_logout : Button = view.findViewById(R.id.logOutButton)

        val title_transport: TextView = view.findViewById(R.id.transportation_title)


        val titles = listOf("Nueva Servicio", "En dirección a ","En dirección a ")
        val directions = listOf("calle 123, #44 -98", "calle2 ","calle 123, #44 -98")
        val my_name = "Sandra"
        val my_rating = "4,5"
        val my_method = "Cash"
        val adapter = CustomAdapterCard(titles, directions)
        val eje1: Transport = Transport("Vehicle 1", "Automobile", "ABC123", "Chebrolete")
        val my_transports = listOf<Transport>(eje1)
        val adapter_vehicle = CustomVehicleCard(my_transports)





        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        recylceViewCars.layoutManager = LinearLayoutManager(requireContext())
        recylceViewCars.adapter = adapter_vehicle


        name.setText(my_name)
        rating.setText(my_rating)
        method_p.setText(my_method)

        button_logout.setOnClickListener {
            val intent = Intent(requireContext(), StartActivity::class.java)
            startActivity(intent)
        }



        viewModel.isSwitchChecked.observe(viewLifecycleOwner) { isChecked ->
            if(isChecked){
                recylceViewCars.visibility = View.GONE
                title_transport.visibility = View.GONE
                add_car_button.visibility = View.GONE

            }
            else{
                recylceViewCars.visibility = View.VISIBLE


            }
        }


    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *

         * @return A new instance of fragment profilePassengerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ProfilePassengerFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}