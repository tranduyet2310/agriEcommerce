package com.example.argiecommerce.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.model.CategoryItem

class CategoryAdapter(private val dataList: ArrayList<CategoryItem>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolderClass>() {

    var onClick: ((CategoryItem) -> Unit)? = null
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
        val categoryItem = dataList[position]
        holder.image.setImageResource(categoryItem.categoryImage)
        holder.title.text = categoryItem.categoryTitle
        holder.itemView.setOnClickListener {
            onClick?.invoke(categoryItem)
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
        val image: ImageView = itemView.findViewById(R.id.image_category)
        val title: TextView = itemView.findViewById(R.id.tvCategoryTitle)
    }
}