package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.model.Subcategory

class SubcategoryAdapter(private val dataList: ArrayList<Subcategory>) :
    RecyclerView.Adapter<SubcategoryAdapter.ViewHolderClass>() {

    var onClick: ((Subcategory) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.category_rv_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val subcategory = dataList[position]
        holder.title.text = subcategory.subcategoryName
        holder.itemView.setOnClickListener {
            onClick?.invoke(subcategory)
        }
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvCategoryTitle)
    }
}