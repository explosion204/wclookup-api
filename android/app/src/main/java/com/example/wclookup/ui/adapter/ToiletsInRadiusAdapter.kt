package com.example.wclookup.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wclookup.core.model.Toilet
import com.example.wclookup.databinding.FragmentToiletInRadiusBinding


class ToiletsInRadiusAdapter(
    private val toilets: List<Toilet>,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<ToiletsInRadiusAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(toilet: Toilet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentToiletInRadiusBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val toilet = toilets[position]
        holder.addressView.text = toilet.address
        holder.scheduleView.text = toilet.schedule
        holder.ratingView.text = toilet.rating.toString()
        holder.itemView.setOnClickListener {
            listener.onItemClick(toilet)
        }
    }

    override fun getItemCount(): Int = toilets.size

    inner class ViewHolder(binding: FragmentToiletInRadiusBinding) : RecyclerView.ViewHolder(binding.root) {
        val addressView: TextView = binding.address
        val scheduleView: TextView = binding.schedule
        val ratingView: TextView = binding.rating
    }

}