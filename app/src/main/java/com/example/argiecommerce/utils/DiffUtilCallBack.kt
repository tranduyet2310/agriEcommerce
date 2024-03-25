package com.example.argiecommerce.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.argiecommerce.model.Product

class DiffUtilCallBack: DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.equals(newItem)
    }
}