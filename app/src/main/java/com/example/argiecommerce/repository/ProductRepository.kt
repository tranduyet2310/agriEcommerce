package com.example.argiecommerce.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.network.AllProductsPagingSource
import com.example.argiecommerce.network.DiscountProductPagingSource
import com.example.argiecommerce.network.SubcategoryProductPagingSource
import com.example.argiecommerce.network.ProductPagingSource
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.network.UpcomingProductPagingSource
import com.example.argiecommerce.utils.Constants.DEFAULT_PAGE_SIZE
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class ProductRepository(context: Context) {
    private val apiService = RetrofitClient.getInstance().getApi()
    fun getProductByCategory(productApiRequest: ProductApiRequest): Flow<PagingData<Product>> {
        return Pager(
            PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE.toInt(),
                enablePlaceholders = false
            )
        ) {
            ProductPagingSource(apiService, productApiRequest)
        }.flow
    }

    fun getAllProducts(productApiRequest: ProductApiRequest): Flow<PagingData<Product>> {
        return Pager(
            PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE.toInt(),
                enablePlaceholders = false
            )
        ) {
            AllProductsPagingSource(apiService, productApiRequest)
        }.flow
    }

    fun getProductBySubCategory(productApiRequest: ProductApiRequest): Flow<PagingData<Product>>{
        return Pager(
            PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE.toInt(),
                enablePlaceholders = false
            )
        ) {
            SubcategoryProductPagingSource(apiService, productApiRequest)
        }.flow
    }

    fun getProductsWithDiscount(productApiRequest: ProductApiRequest): Flow<PagingData<Product>>{
        return Pager(
            PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE.toInt(),
                enablePlaceholders = false
            )
        ) {
            DiscountProductPagingSource(apiService, productApiRequest)
        }.flow
    }

    fun getUpcomingProducts(productApiRequest: ProductApiRequest): Flow<PagingData<Product>>{
        return Pager(
            PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE.toInt(),
                enablePlaceholders = false
            )
        ) {
            UpcomingProductPagingSource(apiService, productApiRequest)
        }.flow
    }
}
