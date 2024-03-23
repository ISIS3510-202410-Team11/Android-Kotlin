package com.example.shareride.activities.mainActivity.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shareride.databinding.CreateRideBinding
import com.google.firebase.database.FirebaseDatabase

class CreateRideFragment : Fragment() {


    private var _binding: CreateRideBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateRideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



            binding.buttonCreateRide.setOnClickListener {

                val departureLocation = binding.editTextDepartureLocation.text.toString().trim()
                val destinationLocation = binding.editTextDestinationLocation.text.toString().trim()
                val price = binding.editTextPrice.text.toString().trim()
                val date = binding.editTextDate.text.toString().trim()
                val time = binding.editTextTime.text.toString().trim()

                val ride = Ride(departureLocation, destinationLocation, price, date, time)


                val databaseReference = FirebaseDatabase.getInstance().getReference("rides")
                val rideId = databaseReference.push().key
                
                rideId?.let {
                    databaseReference.child(it).setValue(ride).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Ride created successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to create ride", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        data class Ride(
            val departureLocation: String,
            val destinationLocation: String,
            val price: String,
            val date: String,
            val time: String

        )
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



