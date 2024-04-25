package com.example.shareride.activities.custom_cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R
import com.example.shareride.clases.Location

class CustomAdapterCard(private val locations: MutableLiveData<List<Location?>?>,
                        private val onItemClick: (String) -> Unit ) :
    RecyclerView.Adapter<CustomAdapterCard.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_historical, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locations.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {

        val locationList = locations.value ?: return

        val location = locationList[i]
        if (location == null) return

        val title = location?.destination ?: ""
        val idTrip = "${location?.latitud} ${location?.longitud}"
        holder.bind(title, idTrip)
        holder.itemView.setOnClickListener {
            onItemClick(title)

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.item_name)
        var itemDirection: TextView = itemView.findViewById(R.id.item_dir)

        fun bind(title: String, direction: String) {
            var actualTitle = title
            var actualDir = direction

            if (title.length > 25) {
                actualTitle = title.substring(0, 25)
            }
            if (direction.length > 25) {
                actualDir = direction.substring(0, 25)
            }
            itemName.text = actualTitle
            itemDirection.text = actualDir
        }
    }
}