package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.model.CategoryItem
import de.hdodenhof.circleimageview.CircleImageView

class CategoryAdapter(private val dataList: ArrayList<CategoryItem>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_list_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.image.setImageResource(currentItem.categoryImage)
        holder.title.text = currentItem.categoryTitle
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image_category)
        val title: TextView = itemView.findViewById(R.id.tvCategoryTitle)
    }
}