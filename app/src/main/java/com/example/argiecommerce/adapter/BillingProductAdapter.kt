package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.argiecommerce.databinding.BillingProductsRvItemBinding
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.Utils.Companion.formatPrice

class BillingProductAdapter(
    private val cartItemList: ArrayList<CartResponse>
): RecyclerView.Adapter<BillingProductAdapter.ViewHolderClass>() {
    inner class ViewHolderClass(
        val binding: BillingProductsRvItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartResponse: CartResponse) {
            binding.apply {
                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(16))
                val imageUrl = cartResponse.product.productImage[0].imageUrl
                val modifiedUrl = imageUrl.replace("http://", "https://")

                GlideApp.with(itemView)
//                    .load(imageUrl)
                    .load(modifiedUrl)
                    .apply(requestOptions)
                    .skipMemoryCache(true)
                    .into(imageCartProduct)

                tvProductCartName.text = cartResponse.product.productName
                tvBillingProductQuantity.text = cartResponse.quantity.toString()
                if (cartResponse.product.discountPrice > 0) {
                    tvProductCartPrice.text = cartResponse.product.discountPrice.formatPrice()
                } else tvProductCartPrice.text = cartResponse.product.standardPrice.formatPrice()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(
            BillingProductsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val cartResponse = cartItemList[position]
        holder.bind(cartResponse)
    }
}