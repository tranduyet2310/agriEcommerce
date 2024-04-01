package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.CartProduct
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.FavoriteResponse
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.repository.CartRepository
import com.example.argiecommerce.utils.ScreenState

class CartViewModel(
    private val cartRepository: CartRepository = CartRepository()
) : ViewModel() {
    lateinit var products: ArrayList<CartProduct>

    fun addToCart(
        token: String,
        userId: Long,
        productId: Long
    ): LiveData<ScreenState<CartResponse?>> {
        return cartRepository.addToCart(token, userId, productId)
    }

    fun getAllCartItems(userId: Long): LiveData<ScreenState<ArrayList<CartResponse>?>>{
        return cartRepository.getAllCartItems(userId)
    }

    fun changeQuantity(
        token: String,
        userId: Long,
        productId: Long,
        quantity: Int
    ): LiveData<ScreenState<CartResponse?>> {
        return cartRepository.changeQuantity(token, userId, productId, quantity)
    }

    fun removeFromCart(
        token: String,
        userId: Long,
        productId: Long
    ): LiveData<ScreenState<MessageResponse?>> {
        return cartRepository.removeFromCart(token, userId, productId)
    }
}