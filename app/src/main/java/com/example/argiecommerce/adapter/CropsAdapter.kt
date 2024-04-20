package com.example.argiecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.databinding.CropsDetailListItemBinding
import com.example.argiecommerce.model.CropsInfo
import com.example.argiecommerce.utils.Constants.KG_UNIT
import com.example.argiecommerce.utils.Constants.TAN_UNIT
import com.example.argiecommerce.utils.Constants.TA_UNIT
import com.example.argiecommerce.utils.Constants.YEN_UNIT
import com.example.argiecommerce.utils.Utils.Companion.formatPrice

class CropsAdapter(private val dataList: ArrayList<CropsInfo>) :
    RecyclerView.Adapter<CropsAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(
            CropsDetailListItemBinding.inflate(LayoutInflater.from(parent.context)),
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

    class ViewHolderClass(binding: CropsDetailListItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        val tvPlant = binding.tvPlant
        val tvCurrent = binding.tvCurrent
        val tvTotal = binding.tvTotal
        val progressBar = binding.progressBar
        val tvEstimatePrice = binding.tvEstimatePrice

        fun bind(cropsInfo: CropsInfo) {
            tvPlant.text = cropsInfo.cropsName
            tvCurrent.text = convertToMassUnit(cropsInfo.currentYield)
            tvTotal.text = convertToMassUnit(cropsInfo.estimateYield)
            tvEstimatePrice.text = cropsInfo.estimatePrice.formatPrice()

            if (cropsInfo.estimateYield != 0.0){
                val currentProgress: Int = (cropsInfo.currentYield * 100 / cropsInfo.estimateYield).toInt()
                progressBar.progress = currentProgress
            } else {
                progressBar.progress = 0
            }
            progressBar.max = 100
        }

        private fun convertToMassUnit(value: Double): String {
            val convertValue: Double
            val yieldText: String

            if (value >= 1000) {
                convertValue = value / 1000
                yieldText = "${convertValue} " + TAN_UNIT
            } else if (value >= 100) {
                convertValue = value / 100
                yieldText = "${convertValue} " + TA_UNIT
            } else if (value >= 10) {
                convertValue = value / 10
                yieldText = "${convertValue} " + YEN_UNIT
            } else {
                yieldText = "${value} " + KG_UNIT
            }
            return yieldText
        }
    }
}