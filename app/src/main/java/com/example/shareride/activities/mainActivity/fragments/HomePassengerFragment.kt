package com.example.shareride.activities.mainActivity.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R
import com.example.shareride.activities.custom_cards.CustomAdapterCard
import com.example.shareride.activities.mainActivity.popUps.popWhereToFragment
import com.example.shareride.activities.trips.TripActivity
import com.google.android.material.search.SearchBar

/**
 * A simple [Fragment] subclass.
 * Use the [HomePassengerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePassengerFragment : Fragment() {

    private  val viewModel: ViewModelMainActivity by activityViewModels()

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
        val sch_bar = view.findViewById<SearchBar>(R.id.where_to)
        val ldg_pop = view.findViewById<ProgressBar>(R.id.poploadingbar)

        val showpopup = popWhereToFragment()



        viewModel.getMostpopularDestination(5)
        val adapter = CustomAdapterCard(viewModel._locationsLVdata){ title ->
            viewModel.updateDestination(title)

            showpopup.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")

        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        recyclerView.adapter = adapter


        sch_bar.setOnClickListener {
            viewModel.clicks_bf_createride("Search")

            showpopup.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")

        }

        viewModel.isPenddingPopLocations.observe(viewLifecycleOwner, Observer { isrequestPending ->
            if(isrequestPending){

                recyclerView.visibility = View.GONE
                ldg_pop.visibility = View.VISIBLE

            }
            else{
                recyclerView.visibility = View.VISIBLE
                ldg_pop.visibility = View.GONE

            }
        })




    }


    private fun openDetailActivity() {
        val intent = Intent(requireContext(), TripActivity::class.java)
        startActivity(intent)
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