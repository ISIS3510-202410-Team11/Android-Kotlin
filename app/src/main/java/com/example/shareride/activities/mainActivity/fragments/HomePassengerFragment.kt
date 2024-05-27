package com.example.shareride.activities.mainActivity.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
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
import com.example.shareride.connectivity.ConnectivityObserver
import com.example.shareride.databinding.FragmentHomePassengerBinding
import com.google.android.material.search.SearchBar

/**
 * A simple [Fragment] subclass.
 * Use the [HomePassengerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePassengerFragment : Fragment() {

    private  val viewModel: ViewModelMainActivity by activityViewModels()
    private lateinit var binding: FragmentHomePassengerBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var sch_bar: SearchBar
    private lateinit var ldg_pop: ProgressBar
    private lateinit var txt_title: TextView
    private lateinit var pop_space: LinearLayout
    private lateinit var no_int_space: LinearLayout
    private lateinit var showpopup: popWhereToFragment

    private  lateinit var myIntent: Intent

    private  var response : Boolean = false


    override fun onCreate(  savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomePassengerBinding.inflate(layoutInflater)



    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomePassengerBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
         sch_bar = binding.whereTo
         ldg_pop = binding.poploadingbar
         txt_title = binding.popdestiTitle
         pop_space = binding.poplocations
         no_int_space = binding.NoIntenernet

         showpopup = popWhereToFragment()


        viewModel.connectivityStatus.observe(viewLifecycleOwner){status ->
            when (status) {

                ConnectivityObserver.Status.Unavailable ,  ConnectivityObserver.Status.Lost-> {
                    viewModel.getcachePopLocations(){
                        val adapter = CustomAdapterCard(it){ title ->
                            viewModel.destination.value = title


                            showpopup.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")

                        }

                        recyclerView.layoutManager = LinearLayoutManager(requireContext())


                        recyclerView.adapter = adapter
                    }




                }

                ConnectivityObserver.Status.Avalilable, ConnectivityObserver.Status.Losing -> {


                    viewModel.fetchAndCachePopLocations(){

                        val adapter = CustomAdapterCard(it){ title ->
                            viewModel.updateDestination(title)

                            showpopup.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")

                        }

                        recyclerView.layoutManager = LinearLayoutManager(requireContext())


                        recyclerView.adapter = adapter

                    }

                }
                }


            }





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
         myIntent = Intent(requireContext(), TripActivity::class.java)
        startActivity(myIntent)
    }


}