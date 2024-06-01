package com.example.argiecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.ProductListItemBinding
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.Utils.Companion.formatPrice

class FavoriteProductAdapter(
    private val context: Context,
    private val dataList: ArrayList<Product>
) : RecyclerView.Adapter<FavoriteProductAdapter.ViewHolderClass>() {

    var onClick: ((Product) -> Unit)? = null
    var onCartClick: ((Product) -> Unit)? = null
    var onFavouriteClick: ((Product) -> Unit)? = null
    var onShareClick: ((Product) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(
            ProductListItemBinding.inflate(LayoutInflater.from(parent.context)),
            context
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val product = dataList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
        holder.imgCart.setOnClickListener {
            if (product.isInCart != 1) {
                holder.imgCart.setImageResource(R.drawable.ic_shopping_cart_green)
                product.isInCart = 1
            } else {
                holder.imgCart.setImageResource(R.drawable.ic_shopping_cart)
                product.isInCart = 0
            }
            onCartClick?.invoke(product)
        }

        holder.imgFavourite.setOnClickListener {
            if (product.isFavourite != 1) {
                holder.imgFavourite.setImageResource(R.drawable.ic_favorite_red)
                product.isFavourite = 1
            } else {
                holder.imgFavourite.setImageResource(R.drawable.ic_favorite_border)
                product.isFavourite = 0
            }
            onFavouriteClick?.invoke(product)
        }

        holder.imgShare.setOnClickListener {
            onShareClick?.invoke(product)
        }
    }

    class ViewHolderClass(
        private val binding: ProductListItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        val imgCart = binding.imgCart
        val imgFavourite = binding.imgFavourite
        val imgShare = binding.imgShare

        fun bind(product: Product) {
            binding.apply {
                tvProductName.text = product.productName
                if (product.discountPrice > 0) {
                    tvProductPrice.text = product.discountPrice.formatPrice()
                } else {
                    tvProductPrice.text = product.standardPrice.formatPrice()
                }

                if (product.isNew) {
                    tvUpcoming.visibility = View.VISIBLE
                    tvProductPrice.visibility = View.INVISIBLE
                    tvUpcoming.text = context.resources.getString(R.string.upcomming)
                }

                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(16))

                val imageUrl = product.productImage[0].imageUrl
                val modifiedUrl = imageUrl.replace("http://", "https://")

                GlideApp.with(context)
                    .load(modifiedUrl)
                    .apply(requestOptions)
                    .skipMemoryCache(true)
                    .into(binding.imgProductImage)

                if (product.isInCart == 1) {
                    imgCart.setImageResource(R.drawable.ic_shopping_cart_green)
                } else {
                    imgCart.setImageResource(R.drawable.ic_shopping_cart)
                }

                if (product.isFavourite == 1) {
                    imgFavourite.setImageResource(R.drawable.ic_favorite_red)
                } else {
                    imgFavourite.setImageResource(R.drawable.ic_favorite_border)
                }
            }
        }
    }
}