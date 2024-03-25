package com.example.argiecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.argiecommerce.model.RegisterApiResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRepository {

    fun getRegisterResponseData(user: User): LiveData<ScreenState<RegisterApiResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<RegisterApiResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().createUser(user)
            .enqueue(object : Callback<RegisterApiResponse> {
                override fun onResponse(
                    call: Call<RegisterApiResponse>,
                    response: Response<RegisterApiResponse>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        val message = "Địa chỉ email đã được đăng ký"
                        mutableLiveData.postValue(ScreenState.Error(message, null))
                    }
                }

                override fun onFailure(call: Call<RegisterApiResponse>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })
        return mutableLiveData
    }
}