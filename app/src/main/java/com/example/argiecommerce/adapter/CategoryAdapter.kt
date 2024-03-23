package com.example.argiecommerce.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.argiecommerce.R
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.utils.GlideApp

class CategoryAdapter(
    private val context: Context,
    private val dataList: ArrayList<CategoryApiResponse>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolderClass>() {
    var onClick: ((CategoryApiResponse) -> Unit)? = null
    var pos: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.category_list_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val category = dataList[position]

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(16))

        GlideApp.with(context)
            .load(category.categoryImage)
            .apply(requestOptions)
            .skipMemoryCache(true)
            .into(holder.image)

        holder.title.text = category.categoryName

        holder.itemView.setOnClickListener {
            onClick?.invoke(category)
            pos = position
            notifyDataSetChanged()
        }

        if (pos == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#289C62"))
            holder.title.setTextColor(Color.WHITE)
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F4F4F4"))
            holder.title.setTextColor(Color.BLACK)
        }
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image_category)
        var title: TextView = itemView.findViewById(R.id.tvCategoryTitle)
    }
}