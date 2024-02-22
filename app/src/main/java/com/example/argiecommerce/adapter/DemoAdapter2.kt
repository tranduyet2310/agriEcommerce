package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.SpecialProductListItemBinding
import com.example.argiecommerce.model.Product

class DemoAdapter2(
    private val dataList: ArrayList<Product>,
    private val listener: DemoAdapterOnClickListener
) :
    RecyclerView.Adapter<DemoAdapter2.ViewHolderClass>() {

    private var _binding: SpecialProductListItemBinding? = null
    private val binding get() = _binding!!

    interface DemoAdapterOnClickListener {
        fun onClick(product: Product)
    }

    class ViewHolderClass(val binding: SpecialProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var product: Product
        private lateinit var listener: DemoAdapterOnClickListener
        fun getProduct(product: Product) {
            this.product = product
        }

        fun setListener(listener: DemoAdapterOnClickListener) {
            this.listener = listener
        }

        fun bind(product: Product) {
            binding.tvProductName.text = product.productName
            binding.tvProductPrice.text = String.format("%,.0f", product.productPrice)
            binding.imgProductImage.setImageResource(product.productImage.toInt())

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
        _binding = SpecialProductListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolderClass(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.getProduct(currentItem)
        holder.bind(currentItem)
        holder.setListener(listener)
    }

}