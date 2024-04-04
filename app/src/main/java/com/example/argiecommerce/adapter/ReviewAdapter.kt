package com.example.argiecommerce.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.databinding.ReviewListItemBinding
import com.example.argiecommerce.model.ReviewResponse
import com.example.argiecommerce.utils.DiffUtilReview

class ReviewAdapter() :
    PagingDataAdapter<ReviewResponse, ReviewAdapter.ViewHolderClass>(DiffUtilReview()) {
    class ViewHolderClass(
        private val binding: ReviewListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reviewResponse: ReviewResponse){
            binding.userName.text = reviewResponse.userFullName
            binding.userFeedback.text = reviewResponse.feedBack
            binding.dateOfReview.text = reviewResponse.reviewDate
            binding.rateProduct.rating = reviewResponse.rating.toFloat()
        }
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        getItem(position)?.let { currentItem ->
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(ReviewListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }
}