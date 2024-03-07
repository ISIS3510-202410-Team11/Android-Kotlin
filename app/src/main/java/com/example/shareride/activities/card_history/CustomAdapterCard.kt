package com.example.shareride.activities.card_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shareride.R

class CustomAdapterCard(private val titles: List<String>, private val directions: List<String>) :
    RecyclerView.Adapter<CustomAdapterCard.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_historical, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val title = titles[i]
        val direction = directions[i]
        holder.bind(title, direction)
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