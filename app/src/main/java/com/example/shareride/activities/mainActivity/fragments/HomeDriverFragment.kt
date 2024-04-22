package com.example.shareride.activities.mainActivity.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.shareride.R
import com.example.shareride.activities.logIn.LogInActivity
import com.example.shareride.activities.mainActivity.CreateRideActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeDriverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeDriverFragment : Fragment() {
    // TODO: Rename and change types of parameters



    private val viewModel: ViewModelMainActivity by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_driver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var create_ride_btn: Button  = view.findViewById<Button>(R.id.create_ride)





        create_ride_btn.setOnClickListener {
            //TODO:sacar el create ride fragment




            viewModel.clicks_bf_createride("create")

            var createRide: Intent = Intent(requireContext(), CreateRideActivity::class.java)
            startActivity(createRide)











        }

    }


}