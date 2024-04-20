package com.example.argiecommerce.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.SearchApiRequest
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.network.SearchPagingSource
import com.example.argiecommerce.utils.Constants
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class SearchRepository(context: Context) {
    private val apiService = RetrofitClient.getInstance().getApi()
    fun searchForProduct(searchApiRequest: SearchApiRequest): Flow<PagingData<Product>> {
        return Pager(
            PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE.toInt(),
                enablePlaceholders = false
            )
        ) {
            SearchPagingSource(apiService, searchApiRequest)
        }.flow
    }
}