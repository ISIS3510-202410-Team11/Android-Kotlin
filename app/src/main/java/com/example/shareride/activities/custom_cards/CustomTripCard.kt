package com.example.shareride.activities.custom_cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R
import com.example.shareride.clases.Trip

class CustomTripCard(private var trips: List<Trip>, private val type:String) :RecyclerView.Adapter<CustomTripCard.viewHolderTrip>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderTrip {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_trips_custom, parent, false)
        return viewHolderTrip(view)
    }
    override fun onBindViewHolder(holder: viewHolderTrip, i: Int) {
        val trip=trips[i]
        holder.bind(trip.vehicle, trip.date,trip.price)
    }


    override fun getItemCount(): Int {
        return trips.size
    }

    fun updateTrips(newTrips: List<Trip?>?) {
        trips = newTrips as List<Trip>
        notifyDataSetChanged()
    }

    inner class  viewHolderTrip(itemView: View): RecyclerView.ViewHolder(itemView){
        var item_vehicle: TextView = itemView.findViewById(R.id.item_name)
        var itemTime: TextView = itemView.findViewById(R.id.item_time)
        var itemicon: ImageButton = itemView.findViewById(R.id.icon_item)
        var itemcost: TextView = itemView.findViewById(R.id.cost_item)


        fun bind(vehicle:String, itemtime: String, cost: Double){

            item_vehicle.text = vehicle
            itemTime.text = itemtime.toString()
            itemcost.text = "$ "+cost.toString()+" COP"

            if (type=="Car"){
                itemicon.setImageResource(R.drawable.baseline_drive_eta_24)

            }
            else if(type=="Motorcycle"){
                itemicon.setImageResource(R.drawable.baseline_electric_bike_24)
            }
            else{}


        }



    }

}