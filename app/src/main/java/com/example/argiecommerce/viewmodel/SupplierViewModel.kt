package com.example.argiecommerce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.FieldApiResponse
import com.example.argiecommerce.model.Image
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.SupplierIntroResponse
import com.example.argiecommerce.repository.SupplierRepository
import com.example.argiecommerce.utils.ScreenState
import kotlinx.coroutines.flow.Flow

class SupplierViewModel(application: Application): AndroidViewModel(application) {

    private val supplierRepository = SupplierRepository(application)
    fun getSupplierAvatar(supplierId: Long): LiveData<ScreenState<Image?>> {
        return supplierRepository.getSupplierAvatar(supplierId)
    }

    fun getSupplierIntroInfo(supplierId: Long): LiveData<ScreenState<ArrayList<SupplierIntroResponse>?>>{
        return supplierRepository.getSupplierIntroInfo(supplierId)
    }

    fun getSupplierProducts(supplierId: Long): Flow<PagingData<Product>> {
        return supplierRepository.getSupplierProduct(supplierId).cachedIn(viewModelScope)
    }

    fun getCategoryBySupplierId(supplierId: Long): LiveData<ScreenState<ArrayList<CategoryApiResponse>?>>{
        return supplierRepository.getCategoryBySupplier(supplierId)
    }

    fun getFieldInfo(supplierId: Long): LiveData<ScreenState<ArrayList<FieldApiResponse>?>>{
        return supplierRepository.getFieldInfo(supplierId)
    }
}