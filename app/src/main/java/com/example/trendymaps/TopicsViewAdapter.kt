package com.example.trendymaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TopicsViewAdapter(
    private val topics: ArrayList<String>, private val itemClickListener: ItemClickListener,
) : RecyclerView.Adapter<TopicsViewAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(topic: String, position: Int)
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val topicText: TextView = itemView.findViewById(R.id.topicText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topics[position]
        holder.topicText.text = topic
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(topics[position], position)
        }
    }

    override fun getItemCount(): Int {
        return topics.size
    }
}