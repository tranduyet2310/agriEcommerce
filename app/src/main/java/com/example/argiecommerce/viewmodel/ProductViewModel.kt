package com.example.argiecommerce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductViewModel(application: Application): AndroidViewModel(application) {

    private val productRepo = ProductRepository(application)

    fun getProducts(productApiRequest: ProductApiRequest): Flow<PagingData<Product>> {
        return productRepo.getProductByCategory(productApiRequest).cachedIn(viewModelScope)
    }

    fun getProductBySubCategory(productApiRequest: ProductApiRequest):Flow<PagingData<Product>>{
        return productRepo.getProductBySubCategory(productApiRequest).cachedIn(viewModelScope)
    }

    fun getProductsWithDiscount(productApiRequest: ProductApiRequest):Flow<PagingData<Product>>{
        return productRepo.getProductsWithDiscount(productApiRequest).cachedIn(viewModelScope)
    }

    fun getUpcomingProducts(productApiRequest: ProductApiRequest):Flow<PagingData<Product>>{
        return productRepo.getUpcomingProducts(productApiRequest).cachedIn(viewModelScope)
    }
}