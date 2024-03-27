package com.example.argiecommerce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.SearchApiRequest
import com.example.argiecommerce.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val searchRepository = SearchRepository(application)

    fun searchForProduct(searchApiRequest: SearchApiRequest): Flow<PagingData<Product>> {
        return searchRepository.searchForProduct(searchApiRequest).cachedIn(viewModelScope)
    }
}