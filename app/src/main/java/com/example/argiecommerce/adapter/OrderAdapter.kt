package com.example.argiecommerce.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.databinding.OrderItemBinding
import com.example.argiecommerce.model.OrderResponse
import com.example.argiecommerce.utils.DiffUtilOrder

class OrderAdapter() :
    PagingDataAdapter<OrderResponse, OrderAdapter.ViewHolderClass>(DiffUtilOrder()) {

    var onClick: ((OrderResponse) -> Unit)? = null

    class ViewHolderClass(
        private val binding: OrderItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(orderResponse: OrderResponse){
            binding.tvOrderId.text = orderResponse.orderNumber
            binding.tvOrderDate.text = orderResponse.dateCreated
        }
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        getItem(position)?.let { currentItem ->
            holder.bind(currentItem)
            holder.itemView.setOnClickListener {
                onClick?.invoke(currentItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(OrderItemBinding.inflate(LayoutInflater.from(parent.context)))
    }
}