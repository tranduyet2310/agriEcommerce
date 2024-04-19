package com.example.argiecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.argiecommerce.model.CooperationResponse
import com.example.argiecommerce.model.FavoriteResponse
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CooperationRepository {
    private val apiService = RetrofitClient.getInstance().getApi()
    fun getCooperationByUserId(userId: Long): LiveData<ScreenState<ArrayList<CooperationResponse>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ArrayList<CooperationResponse>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.getCooperationByUserId(userId)
            .enqueue(object : Callback<ArrayList<CooperationResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<CooperationResponse>>,
                    response: Response<ArrayList<CooperationResponse>>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ArrayList<CooperationResponse>>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun getCooperationBySupplierId(supplierId: Long): LiveData<ScreenState<ArrayList<CooperationResponse>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ArrayList<CooperationResponse>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.getCooperationBySupplierId(supplierId)
            .enqueue(object : Callback<ArrayList<CooperationResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<CooperationResponse>>,
                    response: Response<ArrayList<CooperationResponse>>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ArrayList<CooperationResponse>>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun createCooperation(
        token: String,
        supplierId: Long,
        cooperationResponse: CooperationResponse
    ): LiveData<ScreenState<CooperationResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<CooperationResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.createCooperation(token, supplierId, cooperationResponse)
            .enqueue(object : Callback<CooperationResponse> {
                override fun onResponse(
                    call: Call<CooperationResponse>,
                    response: Response<CooperationResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<CooperationResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun updateCooperation(
        token: String,
        cooperationId: Long,
        cooperationResponse: CooperationResponse
    ): LiveData<ScreenState<CooperationResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<CooperationResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.updateCooperation(token, cooperationId, cooperationResponse)
            .enqueue(object : Callback<CooperationResponse> {
                override fun onResponse(
                    call: Call<CooperationResponse>,
                    response: Response<CooperationResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<CooperationResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun updateCooperationStatus(
        token: String,
        cooperationId: Long,
        cooperationResponse: CooperationResponse
    ): LiveData<ScreenState<CooperationResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<CooperationResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.updateCooperationStatus(token, cooperationId, cooperationResponse)
            .enqueue(object : Callback<CooperationResponse> {
                override fun onResponse(
                    call: Call<CooperationResponse>,
                    response: Response<CooperationResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<CooperationResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun updateCooperationAddress(
        token: String,
        cooperationId: Long,
        addressId: Long
    ): LiveData<ScreenState<CooperationResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<CooperationResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.updateCooperationAddress(token, cooperationId, addressId)
            .enqueue(object : Callback<CooperationResponse> {
                override fun onResponse(
                    call: Call<CooperationResponse>,
                    response: Response<CooperationResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<CooperationResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun getCooperationById(cooperationId: Long): LiveData<ScreenState<CooperationResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<CooperationResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.getCooperationById(cooperationId)
            .enqueue(object : Callback<CooperationResponse> {
                override fun onResponse(
                    call: Call<CooperationResponse>,
                    response: Response<CooperationResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<CooperationResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }
}