package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.SpecialProductListItemBinding
import com.example.argiecommerce.model.Product

class SpecialProductAdapter(
    private val dataList: ArrayList<Product>,
    private val listener: DemoAdapterOnClickListener
) : RecyclerView.Adapter<SpecialProductAdapter.ViewHolderClass>() {

    interface DemoAdapterOnClickListener {
        fun onClick(product: Product)
    }

    class ViewHolderClass(val binding: SpecialProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var product: Product
        private lateinit var listener: DemoAdapterOnClickListener

        fun setListener(listener: DemoAdapterOnClickListener) {
            this.listener = listener
        }

        fun bind(product: Product) {
            binding.tvProductName.text = product.productName
            binding.tvProductPrice.text = String.format("%,.0f", product.productPrice)
            binding.imgProductImage.setImageResource(product.productImage.toInt())

            this.product = product
        }

        init {
            itemView.setOnClickListener(this)
            binding.imgFavourite.setOnClickListener(this)
            binding.imgCart.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            when (v?.id) {
                R.id.imgCart -> toggleProductsInCart()
                R.id.imgFavourite -> toggleFavourite()
                R.id.card_view -> listener.onClick(product)
            }
        }

        private fun toggleFavourite() {
            if (product.isFavourite != 1) {
                binding.imgFavourite.setImageResource(R.drawable.ic_favorite_red)
                product.setIsFavourite(true)
            } else {
                binding.imgFavourite.setImageResource(R.drawable.ic_favorite_border)
                product.setIsFavourite(false)
            }
        }

        private fun toggleProductsInCart() {
            if (product.isInCart != 1) {
                binding.imgCart.setImageResource(R.drawable.ic_shopping_cart_green)
                product.setIsInCart(true)
            } else {
                binding.imgCart.setImageResource(R.drawable.ic_shopping_cart)
                product.setIsInCart(false)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(
            SpecialProductListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)
        holder.setListener(listener)
    }

}