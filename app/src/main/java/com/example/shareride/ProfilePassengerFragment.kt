package com.example.shareride

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.activities.card_history.CustomAdapterCard
import com.example.shareride.activities.singUp.SingUpActivity

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilePassengerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
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
        val name : TextView = view.findViewById(R.id.me_label)
        val rating: TextView = view.findViewById(R.id.me_rating)
        val method_p : TextView = view.findViewById(R.id.me_method)
        val button_logout : Button = view.findViewById(R.id.logOutButton)


        val titles = listOf("Nueva Servicio", "En dirección a ","En dirección a ")
        val directions = listOf("calle 123, #44 -98", "calle2 ","calle 123, #44 -98")
        val my_name = "Sandra"
        val my_rating = "4,5"
        val my_method = "Cash"
        val adapter = CustomAdapterCard(titles, directions)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        name.setText(my_name)
        rating.setText(my_rating)
        method_p.setText(my_method)

        button_logout.setOnClickListener {
            val intent = Intent(requireContext(), StartActivity::class.java)
            startActivity(intent)
        }


    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profilePassengerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilePassengerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}