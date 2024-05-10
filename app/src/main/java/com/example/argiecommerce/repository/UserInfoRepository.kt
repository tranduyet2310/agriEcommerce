package com.example.argiecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.argiecommerce.model.PasswordRequest
import com.example.argiecommerce.model.UserApiResponse
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.ScreenState
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoRepository {
    fun getUserInfo(token: String, userId: Long): LiveData<ScreenState<UserApiResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<UserApiResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().getUserInfo(token, userId)
            .enqueue(object : Callback<UserApiResponse> {
                override fun onResponse(
                    call: Call<UserApiResponse>,
                    response: Response<UserApiResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<UserApiResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun uploadAvatar(
        token: String,
        userId: Long,
        file: MultipartBody.Part
    ): LiveData<ScreenState<UserApiResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<UserApiResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().uploadAvatar(token, userId, file)
            .enqueue(object : Callback<UserApiResponse> {
                override fun onResponse(
                    call: Call<UserApiResponse>,
                    response: Response<UserApiResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<UserApiResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun updateBasicInfo(
        token: String,
        userId: Long,
        userDto: UserApiResponse
    ): LiveData<ScreenState<UserApiResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<UserApiResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().updateBasicInfo(token, userId, userDto)
            .enqueue(object : Callback<UserApiResponse> {
                override fun onResponse(
                    call: Call<UserApiResponse>,
                    response: Response<UserApiResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<UserApiResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun changePassword(
        token: String,
        userId: Long,
        password: PasswordRequest
    ): LiveData<ScreenState<UserApiResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<UserApiResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().changePassword(token, userId, password)
            .enqueue(object : Callback<UserApiResponse> {
                override fun onResponse(
                    call: Call<UserApiResponse>,
                    response: Response<UserApiResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<UserApiResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }
}