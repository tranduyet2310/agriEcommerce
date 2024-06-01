package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.FavoriteResponse
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.repository.FavoriteRepository
import com.example.argiecommerce.utils.ScreenState

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository = FavoriteRepository()
) : ViewModel() {
    fun getFavoriteProducts(userId: Long): LiveData<ScreenState<ArrayList<FavoriteResponse>?>> {
        return favoriteRepository.getFavoriteProductsByUserId(userId)
    }

    fun deleteFavoriteProduct(token: String, userId: Long, productId: Long): LiveData<ScreenState<MessageResponse?>> {
        return favoriteRepository.deleteFavoriteProduct(token, userId, productId)
    }

    fun createFavoriteProduct(token: String, userId: Long, productId: Long): LiveData<ScreenState<FavoriteResponse?>> {
        return favoriteRepository.createFavoriteProduct(token, userId, productId)
    }
}