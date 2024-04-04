package com.example.argiecommerce.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.model.OrderDetailResponse
import com.example.argiecommerce.model.OrderRequest
import com.example.argiecommerce.model.OrderResponse
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.network.OrderPagingSource
import com.example.argiecommerce.network.ProductPagingSource
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.ScreenState
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@OptIn(ExperimentalPagingApi::class)
class OrderRepository(context: Context) {
    private val apiService = RetrofitClient.getInstance().getApi()
    fun getDetailsByOrderId(orderId: Long): LiveData<ScreenState<ArrayList<OrderDetailResponse>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ArrayList<OrderDetailResponse>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.getDetailsByOrderId(orderId)
            .enqueue(object : Callback<ArrayList<OrderDetailResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<OrderDetailResponse>>,
                    response: Response<ArrayList<OrderDetailResponse>>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ArrayList<OrderDetailResponse>>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun getOrderByUserId(userId: Long): Flow<PagingData<OrderResponse>> {
        return Pager(
            PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE.toInt(),
                enablePlaceholders = false
            )
        ) {
            OrderPagingSource(apiService, userId)
        }.flow
    }

    fun updateOrderStatus(token: String, orderId: Long, orderStatus: String): LiveData<ScreenState<OrderResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<OrderResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.updateOrderStatus(token, orderId, orderStatus)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun checkUserPurchasedOrNot(userId: Long, productId: Long): LiveData<ScreenState<MessageResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<MessageResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.checkUserPurchasedOrNot(userId, productId)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }
}