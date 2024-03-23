package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.model.ProductApiResponse
import com.example.argiecommerce.repository.ProductRepository
import com.example.argiecommerce.utils.ScreenState

class ProductViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {
    fun getProductByCategoryId(productApiRequest: ProductApiRequest): LiveData<ScreenState<ProductApiResponse?>> {
        return productRepository.getProductByCategory(productApiRequest)
    }
//    fun getFlashSaleProducts(productApiRequest: ProductApiRequest): LiveData<ScreenState<ProductApiResponse?>> {
//        return productRepository.getProductByCategory(productApiRequest)
//    }
}