package com.example.shareride.activities.custom_cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R

class CustomAdapterCard(private val locations: List<String>?,
                        private val onItemClick: (String) -> Unit ) :
    RecyclerView.Adapter<CustomAdapterCard.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_historical, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locations?.size ?: 0
    }



    override fun onBindViewHolder(holder: ViewHolder, i: Int) {

        val locationList = locations ?: return

        val location = locationList[i]
        if (location == null) return




        holder.bind(location)
        holder.itemView.setOnClickListener {
            onItemClick(location)
            println("click in title")

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.item_name)
        fun bind(title: String) {
            var actualTitle = title

            if (title.length > 25) {
                actualTitle = title.substring(0, 25)
            }

            itemName.text = actualTitle
        }
    }
}