package com.example.argiecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryRepository {
    fun getCategoryResponseDate(): LiveData<ScreenState<List<CategoryApiResponse>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<List<CategoryApiResponse>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().getAllCategories()
            .enqueue(object : Callback<List<CategoryApiResponse>> {
                override fun onResponse(
                    call: Call<List<CategoryApiResponse>>,
                    response: Response<List<CategoryApiResponse>>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    }
                }

                override fun onFailure(call: Call<List<CategoryApiResponse>>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }
}