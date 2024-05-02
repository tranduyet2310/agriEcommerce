package com.example.argiecommerce.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.argiecommerce.model.CooperativePayment
import com.example.argiecommerce.network.CooperativeOrderPagingSource
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class CooperativeOrderRepository(context: Context) {
    private val apiService = RetrofitClient.getInstance().getApi()

    fun getCooperativeOrderByUserId(userId: Long): Flow<PagingData<CooperativePayment>> {
        return Pager(
            PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE.toInt(),
                enablePlaceholders = false
            )
        ) {
            CooperativeOrderPagingSource(apiService, userId)
        }.flow
    }
}