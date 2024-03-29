package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.argiecommerce.databinding.CartProductListItemBinding
import com.example.argiecommerce.model.CartProduct
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.Utils.Companion.formatPrice

class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {
    var onProductClick: ((CartProduct) -> Unit)? = null
    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusClick: ((CartProduct) -> Unit)? = null

    inner class CartProductViewHolder(
        val binding: CartProductListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(16))
                val imageUrl = cartProduct.product.productImage[0].imageUrl
                GlideApp.with(itemView)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .skipMemoryCache(true)
                    .into(imageCartProduct)

                tvProductCartName.text = cartProduct.product.productName
                tvCartProductQuantity.text = cartProduct.quantity.toString()
                if (cartProduct.product.discountPrice > 0) {
                    tvProductCartPrice.text = cartProduct.product.discountPrice.formatPrice()
                } else tvProductCartPrice.text = cartProduct.product.standardPrice.formatPrice()
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.equals(newItem)
        }

        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.productId == newItem.product.productId
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            CartProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)
        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartProduct)
        }
        holder.binding.imagePlus.setOnClickListener{
            onPlusClick?.invoke(cartProduct)
        }
        holder.binding.imageMinus.setOnClickListener {
            onMinusClick?.invoke(cartProduct)
        }
    }
}