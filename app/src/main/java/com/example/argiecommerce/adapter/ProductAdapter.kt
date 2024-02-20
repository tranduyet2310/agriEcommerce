package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.model.Product

class ProductAdapter(private val dataList: ArrayList<Product>) :
    PagedListAdapter<Product, ProductAdapter.ViewHolderClass>(
        DIFF_CALLBACK
    ) {
    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tvProductName)
        val productPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val productImage: ImageView = itemView.findViewById(R.id.imgProductImage)
        val btnShare: ImageView = itemView.findViewById(R.id.imgShare)
        val btnFavourite: ImageView = itemView.findViewById(R.id.imgFavourite)
        val btnAdd: ImageView = itemView.findViewById(R.id.imgCart)

        fun bind(product: Product) {
            productName.text = product.productName
            productPrice.text = String.format("%,.2f", product.productPrice)
            productImage.setImageResource(product.productImage.toInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val product = getItem(position)
        if (product != null) {
            holder.bind(product)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}