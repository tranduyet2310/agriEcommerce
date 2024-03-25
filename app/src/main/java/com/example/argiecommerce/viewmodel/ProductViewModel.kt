package com.example.argiecommerce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.model.ProductApiResponse
import com.example.argiecommerce.repository.ProductRepository
import com.example.argiecommerce.utils.ScreenState
import kotlinx.coroutines.flow.Flow

//class ProductViewModel(
//    private val productRepository: ProductRepository = ProductRepository()
//) : ViewModel() {
//    fun getProductByCategoryId(productApiRequest: ProductApiRequest): LiveData<ScreenState<ProductApiResponse?>> {
//        return productRepository.getProductByCategory(productApiRequest)
//    }
////    fun getFlashSaleProducts(productApiRequest: ProductApiRequest): LiveData<ScreenState<ProductApiResponse?>> {
////        return productRepository.getProductByCategory(productApiRequest)
////    }
//}

class ProductViewModel(application: Application): AndroidViewModel(application) {

    private val productRepo = ProductRepository(application)

    fun getProducts(productApiRequest: ProductApiRequest): Flow<PagingData<Product>> {
        return productRepo.getProductByCategory(productApiRequest).cachedIn(viewModelScope)
    }
}