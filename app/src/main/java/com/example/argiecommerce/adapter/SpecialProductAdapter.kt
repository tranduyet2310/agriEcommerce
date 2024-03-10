package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.ProductListItemBinding
import com.example.argiecommerce.model.Product

class StandardAdapter(private val dataList: ArrayList<Product>) :
    RecyclerView.Adapter<StandardAdapter.StandardViewHolder>() {

    var onClick: ((Product) -> Unit)? = null

    inner class StandardViewHolder(private val binding: ProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var product: Product
        fun bind(product: Product) {
            binding.apply {
                tvProductName.text = product.productName
                tvProductPrice.text = String.format("%,.0f", product.productPrice)
                imgProductImage.setImageResource(product.productImage.toInt())
            }
            this.product = product
        }

        init {
            binding.imgFavourite.setOnClickListener {
                if (product.isFavourite != 1) {
                    binding.imgFavourite.setImageResource(R.drawable.ic_favorite_red)
                    product.setIsFavourite(true)
                } else {
                    binding.imgFavourite.setImageResource(R.drawable.ic_favorite_border)
                    product.setIsFavourite(false)
                }
            }
            binding.imgCart.setOnClickListener {
                if (product.isInCart != 1) {
                    binding.imgCart.setImageResource(R.drawable.ic_shopping_cart_green)
                    product.setIsInCart(true)
                } else {
                    binding.imgCart.setImageResource(R.drawable.ic_shopping_cart)
                    product.setIsInCart(false)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandardViewHolder {
        return StandardViewHolder(ProductListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: StandardViewHolder, position: Int) {
        val product = dataList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }
}