package com.example.argiecommerce.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.argiecommerce.model.ReviewResponse

class DiffUtilReview: DiffUtil.ItemCallback<ReviewResponse>() {
    override fun areItemsTheSame(oldItem: ReviewResponse, newItem: ReviewResponse): Boolean {
        return oldItem.reviewId == newItem.reviewId
    }

    override fun areContentsTheSame(oldItem: ReviewResponse, newItem: ReviewResponse): Boolean {
        return oldItem.equals(newItem)
    }
}