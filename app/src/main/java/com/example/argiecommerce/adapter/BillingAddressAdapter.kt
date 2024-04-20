package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.AddressRvItemBinding
import com.example.argiecommerce.model.UserAddress

class BillingAddressAdapter(
    private val dataList: ArrayList<UserAddress>
) : RecyclerView.Adapter<BillingAddressAdapter.ViewHolderClass>() {

    var onClick: ((UserAddress) -> Unit)? = null
    var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(AddressRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val userAddress = dataList[position]
        holder.bind(userAddress, selectedPosition == position)
        holder.buttonAddress.setOnClickListener {
            if (selectedPosition >= 0) notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onClick?.invoke(userAddress)
        }
    }

    class ViewHolderClass(binding: AddressRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val buttonAddress = binding.buttonAddress
        fun bind(userAddress: UserAddress, isSelected: Boolean) {
            buttonAddress.text = userAddress.contactName
            if (isSelected){
                buttonAddress.setBackgroundResource(R.drawable.selected_button_background)
            } else {
                buttonAddress.setBackgroundResource(R.drawable.unselected_button_background)
            }
        }
    }
}