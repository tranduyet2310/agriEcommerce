package com.example.argiecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.argiecommerce.model.OrderDetailResponse
import com.example.argiecommerce.model.OrderRequest
import com.example.argiecommerce.model.OrderResponse
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository {
    fun getDetailsByOrderId(orderId: Long): LiveData<ScreenState<ArrayList<OrderDetailResponse>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ArrayList<OrderDetailResponse>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().getDetailsByOrderId(orderId)
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

    fun getOrderByUserId(userId: Long): LiveData<ScreenState<ArrayList<OrderResponse>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ArrayList<OrderResponse>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().getOrderByUserId(userId)
            .enqueue(object : Callback<ArrayList<OrderResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<OrderResponse>>,
                    response: Response<ArrayList<OrderResponse>>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ArrayList<OrderResponse>>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

}