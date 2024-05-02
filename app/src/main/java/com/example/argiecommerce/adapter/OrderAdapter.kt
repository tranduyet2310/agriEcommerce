package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.argiecommerce.databinding.OrderItemBinding
import com.example.argiecommerce.model.OrderResponse
import com.example.argiecommerce.utils.DiffUtilOrder
import com.example.argiecommerce.utils.GlideApp

class OrderAdapter() :
    PagingDataAdapter<OrderResponse, OrderAdapter.ViewHolderClass>(DiffUtilOrder()) {

    var onClick: ((OrderResponse) -> Unit)? = null

    class ViewHolderClass(
        private val binding: OrderItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        val imgProduct = binding.imgProduct
        val tvOrderDate = binding.tvOrderDate
        val tvOuantity = binding.tvOuantity
        val tvProductName = binding.tvProductName

        fun bind(orderResponse: OrderResponse){
            val dateCreated = "Ngày tạo: ${orderResponse.dateCreated}"

            if (!orderResponse.orderDetails.isEmpty()){
                val firstProduct = orderResponse.orderDetails.get(0)
                val quanity = "Số lượng: ${firstProduct.quantity}"
                tvOuantity.text = quanity
                tvProductName.text = firstProduct.product.productName

                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(16))
                val imageUrl = firstProduct.product.productImage[0].imageUrl

                GlideApp.with(itemView)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .skipMemoryCache(true)
                    .into(imgProduct)
            }

            tvOrderDate.text = dateCreated
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