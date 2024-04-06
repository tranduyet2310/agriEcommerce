package com.example.argiecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.databinding.CropsListItemBinding
import com.example.argiecommerce.model.CropsInfo
import com.example.argiecommerce.utils.Constants.KG_UNIT
import com.example.argiecommerce.utils.Constants.TAN_UNIT
import com.example.argiecommerce.utils.Constants.TA_UNIT
import com.example.argiecommerce.utils.Constants.YEN_UNIT

class CropsAdapter(private val dataList: ArrayList<CropsInfo>) :
    RecyclerView.Adapter<CropsAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(
            CropsListItemBinding.inflate(LayoutInflater.from(parent.context)),
            parent.context
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)
    }

    class ViewHolderClass(binding: CropsListItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        val cropsName = binding.tvCropsName
        val cropsYield = binding.tvCropsYield
        fun bind(cropsInfo: CropsInfo) {

            val yield = cropsInfo.estimateYield
            val convertValue: Double
            val yieldText: String

            if (yield >= 1000) {
                convertValue = yield / 1000
                yieldText = "${convertValue} " + TAN_UNIT
            } else if (yield >= 100) {
                convertValue = yield / 100
                yieldText = "${convertValue} " + TA_UNIT
            } else if (yield >= 10) {
                convertValue = yield / 10
                yieldText = "${convertValue} " + YEN_UNIT
            } else {
                yieldText = "${yield} " + KG_UNIT
            }

            cropsName.text = cropsInfo.cropsName
            cropsYield.text = yieldText
        }
    }
}