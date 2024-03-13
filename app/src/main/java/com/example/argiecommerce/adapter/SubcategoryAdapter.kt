package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R

class SubcategoryAdapter(private val dataList: ArrayList<String>) :
    RecyclerView.Adapter<SubcategoryAdapter.ViewHolderClass>() {

    var onClick: ((String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.category_rv_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val title = dataList[position]
        holder.title.text = title
        holder.itemView.setOnClickListener {
            onClick?.invoke(title)
        }
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvCategoryTitle)
    }
}