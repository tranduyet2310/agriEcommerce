package com.example.argiecommerce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.argiecommerce.model.OrderResponse
import com.example.argiecommerce.model.ReviewRequest
import com.example.argiecommerce.model.ReviewResponse
import com.example.argiecommerce.model.ReviewStatisticResponse
import com.example.argiecommerce.repository.ReviewRepository
import com.example.argiecommerce.utils.ScreenState
import kotlinx.coroutines.flow.Flow

class ReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val reviewRepository: ReviewRepository = ReviewRepository(application)
    fun getAllReivewsByProductId(productId: Long): Flow<PagingData<ReviewResponse>> {
        return reviewRepository.getAllReivewsByProductId(productId).cachedIn(viewModelScope)
    }

    fun createReviews(
        token: String,
        userId: Long,
        productId: Long,
        reviewRequest: ReviewRequest
    ): LiveData<ScreenState<ReviewResponse?>> {
        return reviewRepository.createReviews(token, userId, productId, reviewRequest)
    }

    fun calculateTotalRating(productId: Long): LiveData<ScreenState<ReviewStatisticResponse?>> {
        return reviewRepository.calculateTotalRating(productId)
    }

    fun statisticRating(productId: Long): LiveData<ScreenState<ReviewStatisticResponse?>> {
        return reviewRepository.statisticRating(productId)
    }

    fun averageRating(productId: Long): LiveData<ScreenState<ReviewStatisticResponse?>> {
        return reviewRepository.averageRating(productId)
    }

    fun supplierAverageRating(supplierId: Long): LiveData<ScreenState<ReviewStatisticResponse?>> {
        return reviewRepository.supplierAverageRating(supplierId)
    }
}