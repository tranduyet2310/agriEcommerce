package com.example.argiecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.UpcomingProductListItemBinding
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.DiffUtilCallBack
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.ProgressDialog

class UpCommingProductAdapter(
    private val context: Context,
    private val user: User?
) : PagingDataAdapter<Product, UpCommingProductAdapter.ViewHolderClass>(DiffUtilCallBack()) {

    var onClick: ((Product) -> Unit)? = null
    var onCartClick: ((Product) -> Unit)? = null
    var onFavouriteClick: ((Product) -> Unit)? = null
    var onShareClick: ((Product) -> Unit)? = null

    class ViewHolderClass(
        private val binding: UpcomingProductListItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root){

        val imgCart = binding.imgCart
        val imgFavourite = binding.imgFavourite
        val imgShare = binding.imgShare

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(UpcomingProductListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false), context)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        getItem(position)?.let { currentItem ->
            holder.bind(currentItem)
            holder.itemView.setOnClickListener {
                onClick?.invoke(currentItem)
            }
            holder.imgCart.setOnClickListener {
                if (user == null) {
                    val dialog = ProgressDialog.createMessageDialog(context, context.resources.getString(R.string.need_to_login))
                    dialog.show()
                } else {
                    if (currentItem.isInCart != 1) {
                        holder.imgCart.setImageResource(R.drawable.ic_shopping_cart_green)
                        currentItem.isInCart = 1
                    } else {
                        holder.imgCart.setImageResource(R.drawable.ic_shopping_cart)
                        currentItem.isInCart = 0
                    }
                }
                onCartClick?.invoke(currentItem)
            }

            holder.imgFavourite.setOnClickListener {
                if (user == null) {
                    val dialog = ProgressDialog.createMessageDialog(context, context.resources.getString(R.string.need_to_login))
                    dialog.show()
                } else {
                    if (currentItem.isFavourite != 1) {
                        holder.imgFavourite.setImageResource(R.drawable.ic_favorite_red)
                        currentItem.isFavourite = 1
                    } else {
                        holder.imgFavourite.setImageResource(R.drawable.ic_favorite_border)
                        currentItem.isFavourite = 0
                    }
                }
                onFavouriteClick?.invoke(currentItem)
            }

            holder.imgShare.setOnClickListener {
                onShareClick?.invoke(currentItem)
            }
        }
    }
}