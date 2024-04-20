package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.argiecommerce.databinding.CartProductListItemBinding
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.Utils.Companion.formatPrice

class CartProductAdapter(
    private val cartItemList: ArrayList<CartResponse>
) : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {

    var onProductClick: ((CartResponse) -> Unit)? = null
    var onPlusClick: ((CartResponse) -> Unit)? = null
    var onMinusClick: ((CartResponse) -> Unit)? = null

    inner class CartProductViewHolder(
        val binding: CartProductListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartResponse: CartResponse) {
            binding.apply {
                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(16))
                val imageUrl = cartResponse.product.productImage[0].imageUrl

                GlideApp.with(itemView)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .skipMemoryCache(true)
                    .into(imageCartProduct)

                tvProductCartName.text = cartResponse.product.productName
                tvCartProductQuantity.text = cartResponse.quantity.toString()
                if (cartResponse.product.discountPrice > 0) {
                    tvProductCartPrice.text = cartResponse.product.discountPrice.formatPrice()
                } else tvProductCartPrice.text = cartResponse.product.standardPrice.formatPrice()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            CartProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartResponse = cartItemList[position]
        holder.bind(cartResponse)
        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartResponse)
        }
        holder.binding.imagePlus.setOnClickListener{
            onPlusClick?.invoke(cartResponse)
        }
        holder.binding.imageMinus.setOnClickListener {
            onMinusClick?.invoke(cartResponse)
        }
    }
}