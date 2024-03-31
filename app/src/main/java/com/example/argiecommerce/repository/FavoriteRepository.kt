package com.example.argiecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.argiecommerce.model.FavoriteResponse
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteRepository {
    fun getFavoriteProductsByUserId(userId: Long): LiveData<ScreenState<ArrayList<FavoriteResponse>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ArrayList<FavoriteResponse>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().getFavoriteProducts(userId)
            .enqueue(object : Callback<ArrayList<FavoriteResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<FavoriteResponse>>,
                    response: Response<ArrayList<FavoriteResponse>>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ArrayList<FavoriteResponse>>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun deleteFavoriteProduct(
        token: String,
        userId: Long,
        productId: Long
    ): LiveData<ScreenState<MessageResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<MessageResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().deleteFavoriteProduct(token, userId, productId)
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

    fun createFavoriteProduct(
        token: String,
        userId: Long,
        productId: Long
    ): LiveData<ScreenState<FavoriteResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<FavoriteResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().createFavProduct(token, userId, productId)
            .enqueue(object : Callback<FavoriteResponse> {
                override fun onResponse(
                    call: Call<FavoriteResponse>,
                    response: Response<FavoriteResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }
}