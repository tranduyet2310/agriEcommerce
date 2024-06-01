package com.example.argiecommerce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.model.OrderResponse
import com.example.argiecommerce.repository.OrderRepository
import com.example.argiecommerce.utils.ScreenState
import kotlinx.coroutines.flow.Flow

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val orderRepository: OrderRepository = OrderRepository(application)

    fun getOrderByUserId(userId: Long): Flow<PagingData<OrderResponse>> {
        return orderRepository.getOrderByUserId(userId).cachedIn(viewModelScope)
    }

    fun updateOrderStatus(token: String, orderId: Long, orderStatus: String): LiveData<ScreenState<OrderResponse?>> {
        return orderRepository.updateOrderStatus(token, orderId, orderStatus)
    }

    fun checkUserPurchasedOrNot(userId: Long, productId: Long): LiveData<ScreenState<MessageResponse?>>{
        return orderRepository.checkUserPurchasedOrNot(userId, productId)
    }
}