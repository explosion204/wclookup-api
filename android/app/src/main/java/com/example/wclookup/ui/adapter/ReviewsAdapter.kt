package com.example.wclookup.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.wclookup.core.model.Review
import com.example.wclookup.databinding.FragmentReviewBinding

class ReviewsAdapter(
    private val reviews: MutableLiveData<List<Review>>,
    private val usernames: Map<Long, String>
): RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentReviewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews.value!![position]
        holder.nicknameView.text = usernames[review.id]
        holder.reviewRatingView.text = review.rating.toString()
        holder.reviewView.text = review.text
    }

    override fun getItemCount(): Int = reviews.value!!.size

    inner class ViewHolder(binding: FragmentReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val nicknameView: TextView = binding.nickname
        val reviewRatingView: TextView = binding.reviewRating
        val reviewView: TextView = binding.review
    }
}