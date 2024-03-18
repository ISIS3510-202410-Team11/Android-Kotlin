package com.example.shareride.activities.mainActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R
import com.example.shareride.activities.custom_cards.CustomAdapterCard
import com.example.shareride.activities.mainActivity.popUps.popWhereToFragment
import com.google.android.material.search.SearchBar

/**
 * A simple [Fragment] subclass.
 * Use the [HomePassengerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePassengerFragment : Fragment() {


    override fun onCreate(  savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_passenger, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        val serch_b = view.findViewById<SearchBar>(R.id.where_to)




        val titles = listOf("Universidad de los andes", "Lugar2","Lugar2")
        val directions = listOf("calle 123, #44 -98", "calle2 ","calle 123, #44 -98")
        val adapter = CustomAdapterCard(titles, directions)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        recyclerView.adapter = adapter


        serch_b.setOnClickListener {
            val shopopUp = popWhereToFragment()
            shopopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")

        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HomeDriverFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(  ) =
            HomePassengerFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}