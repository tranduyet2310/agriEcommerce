package com.example.argiecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.model.InfoDialog

class InfoDialogAdapter(private val dataList: ArrayList<InfoDialog>) :
    RecyclerView.Adapter<InfoDialogAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.info_dialog_item, parent, false)
        return ViewHolderClass(itemView, parent.context)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)

    }

    class ViewHolderClass(val itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val fullName: TextView = itemView.findViewById(R.id.tvFullName)
        val crops: TextView = itemView.findViewById(R.id.tvCropsType)
        val state: TextView = itemView.findViewById(R.id.tvState)
        fun bind(infoDialog: InfoDialog) {
            fullName.text = infoDialog.fullName
            crops.text = infoDialog.cropsType

            if (infoDialog.state.equals("Chấp nhận")) {
                state.text = infoDialog.state
                state.setTextColor(context.getColor(R.color.greenAgri))
            } else if (infoDialog.state.equals("Đã hủy")) {
                state.text = infoDialog.state
                state.setTextColor(context.getColor(R.color.redAgri))
            } else {
                state.text = infoDialog.state
                state.setTextColor(context.getColor(R.color.orange))
            }

        }
    }
}