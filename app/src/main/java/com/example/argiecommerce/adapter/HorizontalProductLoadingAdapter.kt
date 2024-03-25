package com.example.argiecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.databinding.HorizontalItemLoadingStateBinding

class HorizontalProductLoadingAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<HorizontalProductLoadingAdapter.LoadingStateViewHolder>() {

    class LoadingStateViewHolder(
        private val binding: HorizontalItemLoadingStateBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                retry()
            }
        }

        fun bindState(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.tvErrorMessage.isVisible = loadState is LoadState.Error
            binding.btnRetry.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val view = HorizontalItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context))
        return LoadingStateViewHolder(view, retry)
    }

}