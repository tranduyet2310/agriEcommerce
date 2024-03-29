package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.databinding.UserAddressListItemBinding
import com.example.argiecommerce.model.UserAddress

class UserAddressAdapter(
    private val dataList: ArrayList<UserAddress>
) : RecyclerView.Adapter<UserAddressAdapter.ViewHolderClass>() {

    var onClick: ((UserAddress) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(UserAddressListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val userAddress = dataList[position]
        holder.bind(userAddress)
        holder.itemView.setOnClickListener {
            onClick?.invoke(userAddress)
        }

    }

    class ViewHolderClass(binding: UserAddressListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvUserAddressName: TextView = binding.tvUserAddressName
        val tvUserAddressPhone: TextView = binding.tvUserAddressPhone
        val tvUserAddressDetail: TextView = binding.tvUserAddressDetail

        fun bind(userAddress: UserAddress) {
            tvUserAddressName.text = userAddress.contactName
            tvUserAddressPhone.text = userAddress.phone
            tvUserAddressDetail.text = userAddress.details
        }
    }
}