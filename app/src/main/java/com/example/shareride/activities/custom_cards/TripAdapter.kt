package com.example.shareride.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R
import com.example.shareride.clases.Trip

class TripAdapter(private val trips: List<Trip>) : RecyclerView.Adapter<TripAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trip = trips[position]
        holder.bind(trip)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.item_name)
        private val itemTime: TextView = itemView.findViewById(R.id.item_time)
        private val costItem: TextView = itemView.findViewById(R.id.cost_item)
        private val iconItem: ImageButton = itemView.findViewById(R.id.icon_item)

        fun bind(trip: Trip) {
            itemName.text = trip.start_location
            itemTime.text = trip.start_time
            costItem.text = trip.price
            // You can set the iconItem's image resource if needed
        }
    }
}
