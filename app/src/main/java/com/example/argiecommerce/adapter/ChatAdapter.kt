package com.example.argiecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.SupplierSearchListItemBinding
import com.example.argiecommerce.model.UserFirebase
import com.example.argiecommerce.utils.GlideApp

class ChatAdapter(
    context: Context,
    supplierList: List<UserFirebase>,
    isChatCheck: Boolean
) : RecyclerView.Adapter<ChatAdapter.ViewHolder?>() {

    private val context: Context
    private val supplierList: List<UserFirebase>
    private val isChatCheck: Boolean
    var onClick: ((UserFirebase) -> Unit)? = null

    init {
        this.context = context
        this.supplierList = supplierList
        this.isChatCheck = isChatCheck
    }

    class ViewHolder(
        binding: SupplierSearchListItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        val tvUserName = binding.tvUserName
        val profileImage = binding.profileImage
        val imgOnline = binding.imgOnline
        val imgOffline = binding.imgOffline
        val lastMessage = binding.tvLastMessage

        fun bind(userFirebase: UserFirebase){
            tvUserName.text = userFirebase.name
            val imageUrl = userFirebase.profileImage
            val modifiedUrl = imageUrl.replace("http://", "https://")
            GlideApp.with(context).load(modifiedUrl)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .skipMemoryCache(true)
                .into(profileImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SupplierSearchListItemBinding.inflate(LayoutInflater.from(parent.context)), context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = supplierList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return supplierList.size
    }
}