package com.example.argiecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.ProductListItemBinding
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.DiffUtilCallBack
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.Utils.Companion.formatPrice

class VerticalProductAdapter(
    private val context: Context,
    private val user: User?
) : PagingDataAdapter<Product, VerticalProductAdapter.ViewHolderClass>(DiffUtilCallBack()) {

    var onClick: ((Product) -> Unit)? = null

    class ViewHolderClass(
        private val binding: ProductListItemBinding,
        private val context: Context,
        private val user: User?
    ) :
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
                if (product.discountPrice > 0){
                    tvProductPrice.text = product.discountPrice.formatPrice()
                } else {
                    tvProductPrice.text = product.standardPrice.formatPrice()
                }

                if (product.isNew){
                    tvUpcoming.visibility = View.VISIBLE
                    tvProductPrice.visibility = View.INVISIBLE
                    tvUpcoming.text = context.resources.getString(R.string.upcomming)
                }

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
            if (user == null){
                val dialog = ProgressDialog.createMessageDialog(context, context.resources.getString(R.string.need_to_login))
                dialog.show()
            } else{
                if (product.isFavourite != 1) {
                    binding.imgFavourite.setImageResource(R.drawable.ic_favorite_red)
                    product.isFavourite = 1
                } else {
                    binding.imgFavourite.setImageResource(R.drawable.ic_favorite_border)
                    product.isFavourite = 0
                }
            }

        }

        private fun toggleProductsInCart() {
            if (user == null){
                val dialog = ProgressDialog.createMessageDialog(context, context.resources.getString(R.string.need_to_login))
                dialog.show()
            } else{
                if (product.isInCart != 1) {
                    binding.imgCart.setImageResource(R.drawable.ic_shopping_cart_green)
                    product.isInCart = 1
                } else {
                    binding.imgCart.setImageResource(R.drawable.ic_shopping_cart)
                    product.isInCart = 0
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(
            ProductListItemBinding.inflate(LayoutInflater.from(parent.context)),
            context, user
        )
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        getItem(position)?.let { currentItem ->
            holder.bind(currentItem)
            holder.itemView.setOnClickListener {
                onClick?.invoke(currentItem)
            }
        }

    }
}