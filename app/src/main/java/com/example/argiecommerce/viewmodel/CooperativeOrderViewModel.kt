package com.example.argiecommerce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.argiecommerce.model.CooperativePayment
import com.example.argiecommerce.repository.CooperativeOrderRepository
import kotlinx.coroutines.flow.Flow

class CooperativeOrderViewModel(application: Application) : AndroidViewModel(application) {
    private val cooperativeOrderRepository: CooperativeOrderRepository = CooperativeOrderRepository(application)

    fun getCooperativeOrderByUserId(userId: Long): Flow<PagingData<CooperativePayment>> {
        return cooperativeOrderRepository.getCooperativeOrderByUserId(userId).cachedIn(viewModelScope)
    }
}