package com.example.argiecommerce.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.argiecommerce.model.OrderResponse

class DiffUtilOrder: DiffUtil.ItemCallback<OrderResponse>() {
    override fun areItemsTheSame(oldItem: OrderResponse, newItem: OrderResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OrderResponse, newItem: OrderResponse): Boolean {
        return oldItem.equals(newItem)
    }
}