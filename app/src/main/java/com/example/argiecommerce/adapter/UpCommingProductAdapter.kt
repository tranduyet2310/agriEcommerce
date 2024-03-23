package com.example.argiecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FlashSaleProductListItemBinding
import com.example.argiecommerce.databinding.ProductListItemBinding
import com.example.argiecommerce.databinding.SpecialProductListItemBinding
import com.example.argiecommerce.databinding.UpcomingProductListItemBinding
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.Utils
import com.example.argiecommerce.utils.Utils.Companion.calculateDiscountPercentage
import com.example.argiecommerce.utils.Utils.Companion.formatPrice

class UpCommingProductAdapter(
    private val context: Context,
    private val dataList: ArrayList<Product>
) : RecyclerView.Adapter<UpCommingProductAdapter.ViewHolderClass>() {

    var onClick: ((Product) -> Unit)? = null

    class ViewHolderClass(val binding: UpcomingProductListItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var product: Product

        init {
            binding.imgFavourite.setOnClickListener(this)
            binding.imgCart.setOnClickListener(this)
            binding.imgShare.setOnClickListener(this)
        }

        fun bind(product: Product) {
            binding.apply {
                tvProductName.text = product.productName
                tvProductPrice.text = context.resources.getString(R.string.upcomming)

                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(16))

                val imageUrl = product.productImage[0].imageUrl

                GlideApp.with(context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .skipMemoryCache(true)
                    .into(binding.imgProductImage)

            }
            this.product = product
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            when (v?.id) {
                R.id.imgCart -> toggleProductsInCart()
                R.id.imgFavourite -> toggleFavourite()
                R.id.imgShare -> shareProduct()
            }
        }

        private fun shareProduct() {
            Toast.makeText(itemView.context, "Clicked", Toast.LENGTH_SHORT).show()
        }

        private fun toggleFavourite() {
            if (product.isFavourite != 1) {
                binding.imgFavourite.setImageResource(R.drawable.ic_favorite_red)
                product.setIsFavourite(1)
            } else {
                binding.imgFavourite.setImageResource(R.drawable.ic_favorite_border)
                product.setIsFavourite(0)
            }
        }

        private fun toggleProductsInCart() {
            if (product.isInCart != 1) {
                binding.imgCart.setImageResource(R.drawable.ic_shopping_cart_green)
                product.setIsInCart(1)
            } else {
                binding.imgCart.setImageResource(R.drawable.ic_shopping_cart)
                product.setIsInCart(0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(
            UpcomingProductListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), context
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentItem)
        }
    }
}