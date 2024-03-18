package com.example.shareride.activities.custom_cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R
import com.example.shareride.clases.Transport

class CustomVehicleCard (private val transports: List<Transport>):
    RecyclerView.Adapter<CustomVehicleCard.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.card_car, parent, false)
    return ViewHolder(view)


    }



    override fun getItemCount(): Int {
        return transports.size


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val transport = transports[position]
            holder.bind(transport)
        }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vehicleName: TextView = itemView.findViewById(R.id.me_vehicle_name)
        var vehicleType: TextView = itemView.findViewById(R.id.me_carType)
        var vehiclePlate: TextView = itemView.findViewById(R.id.me_plate)
        var vehicleReference: TextView = itemView.findViewById(R.id.me_reference)

        fun bind(tranportme : Transport) {

            vehicleName.text = tranportme.name
            vehicleType.text = tranportme.type
            vehiclePlate.text =tranportme.plate
            vehicleReference.text = tranportme.reference

        }
    }


}