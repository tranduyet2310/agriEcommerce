package com.example.argiecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.LoginRequest
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {
    fun getLoginResponseData(loginRequest: LoginRequest): LiveData<ScreenState<LoginApiResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<LoginApiResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().loginUser(loginRequest)
            .enqueue(object : Callback<LoginApiResponse> {
                override fun onResponse(
                    call: Call<LoginApiResponse>,
                    response: Response<LoginApiResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        val message = "Email/Password không đúng"
                        mutableLiveData.postValue(ScreenState.Error(message, null))
                    }
                }

                override fun onFailure(call: Call<LoginApiResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }
}