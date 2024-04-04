package com.example.argiecommerce.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.argiecommerce.model.OrderResponse
import com.example.argiecommerce.model.ReviewRequest
import com.example.argiecommerce.model.ReviewResponse
import com.example.argiecommerce.model.ReviewStatisticResponse
import com.example.argiecommerce.network.OrderPagingSource
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.network.ReviewPagingSource
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.ScreenState
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@OptIn(ExperimentalPagingApi::class)
class ReviewRepository(context: Context) {
    private val apiService = RetrofitClient.getInstance().getApi()
    fun getAllReivewsByProductId(productId: Long): Flow<PagingData<ReviewResponse>> {
        return Pager(
            PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE.toInt(),
                enablePlaceholders = false
            )
        ) {
            ReviewPagingSource(apiService, productId)
        }.flow
    }

    fun createReviews(token: String, userId: Long, productId: Long, reviewRequest: ReviewRequest):
            LiveData<ScreenState<ReviewResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ReviewResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.createReview(token, userId, productId, reviewRequest)
            .enqueue(object : Callback<ReviewResponse> {
                override fun onResponse(
                    call: Call<ReviewResponse>,
                    response: Response<ReviewResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun calculateTotalRating(productId: Long): LiveData<ScreenState<ReviewStatisticResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ReviewStatisticResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.calculateTotalRating(productId)
            .enqueue(object : Callback<ReviewStatisticResponse> {
                override fun onResponse(
                    call: Call<ReviewStatisticResponse>,
                    response: Response<ReviewStatisticResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ReviewStatisticResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun statisticRating(productId: Long): LiveData<ScreenState<ReviewStatisticResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ReviewStatisticResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.statisticRating(productId)
            .enqueue(object : Callback<ReviewStatisticResponse> {
                override fun onResponse(
                    call: Call<ReviewStatisticResponse>,
                    response: Response<ReviewStatisticResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ReviewStatisticResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun averageRating(productId: Long): LiveData<ScreenState<ReviewStatisticResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ReviewStatisticResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.averageRating(productId)
            .enqueue(object : Callback<ReviewStatisticResponse> {
                override fun onResponse(
                    call: Call<ReviewStatisticResponse>,
                    response: Response<ReviewStatisticResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ReviewStatisticResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }
}