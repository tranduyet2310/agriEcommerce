package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.UserAddressListItemBinding
import com.example.argiecommerce.model.UserAddress

class CooperationAddressAdapter (
    private val dataList: ArrayList<UserAddress>
) : RecyclerView.Adapter<CooperationAddressAdapter.ViewHolderClass>() {

    var onClick: ((UserAddress) -> Unit)? = null
    var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(UserAddressListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val userAddress = dataList[position]
        holder.bind(userAddress, selectedPosition == position)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onClick?.invoke(userAddress)
        }
    }

    class ViewHolderClass(binding: UserAddressListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvUserAddressName: TextView = binding.tvUserAddressName
        val tvUserAddressPhone: TextView = binding.tvUserAddressPhone
        val tvUserAddressDetail: TextView = binding.tvUserAddressDetail
        fun bind(userAddress: UserAddress, isSelected: Boolean) {
            tvUserAddressName.text = userAddress.contactName
            tvUserAddressPhone.text = userAddress.phone
            tvUserAddressDetail.text = userAddress.details

            if (isSelected){
                itemView.setBackgroundResource(R.drawable.unselected_button_background)
            }
            else {
                itemView.setBackgroundResource(0)
            }
        }
    }
}