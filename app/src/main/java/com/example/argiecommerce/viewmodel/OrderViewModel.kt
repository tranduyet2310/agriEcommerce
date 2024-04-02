package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.OrderDetailResponse
import com.example.argiecommerce.model.OrderRequest
import com.example.argiecommerce.model.OrderResponse
import com.example.argiecommerce.repository.OrderRepository
import com.example.argiecommerce.utils.ScreenState

class OrderViewModel(
    private val orderRepository: OrderRepository = OrderRepository()
) : ViewModel() {
    fun getOrderByUserId(userId: Long): LiveData<ScreenState<ArrayList<OrderResponse>?>> {
        return orderRepository.getOrderByUserId(userId)
    }

    fun getDetailsByOrderId(orderId: Long): LiveData<ScreenState<ArrayList<OrderDetailResponse>?>> {
        return orderRepository.getDetailsByOrderId(orderId)
    }
}