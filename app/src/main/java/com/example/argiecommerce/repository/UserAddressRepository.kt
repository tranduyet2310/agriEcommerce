package com.example.argiecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants.ADDRESS_ERROR
import com.example.argiecommerce.utils.Constants.MAX_ADDRESS
import com.example.argiecommerce.utils.Constants.SERVER_ERROR
import com.example.argiecommerce.utils.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAddressRepository {
    fun createNewUserAddress(
        token: String,
        userId: Long,
        userAddress: UserAddress
    ): LiveData<ScreenState<UserAddress?>> {
        val mutableLiveData = MutableLiveData<ScreenState<UserAddress?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().createNewAddress(token, userId, userAddress)
            .enqueue(object : Callback<UserAddress> {
                override fun onResponse(
                    call: Call<UserAddress>,
                    response: Response<UserAddress>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(MAX_ADDRESS, null))
                    }
                }

                override fun onFailure(call: Call<UserAddress>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun getAddressByUserId(userId: Long): LiveData<ScreenState<ArrayList<UserAddress>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ArrayList<UserAddress>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi().getAddressByUserId(userId)
            .enqueue(object : Callback<ArrayList<UserAddress>> {
                override fun onResponse(
                    call: Call<ArrayList<UserAddress>>,
                    response: Response<ArrayList<UserAddress>>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserAddress>>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun updateUserAddress(
        token: String,
        userId: Long,
        addressId: Long,
        userAddress: UserAddress
    ): LiveData<ScreenState<UserAddress?>> {
        val mutableLiveData = MutableLiveData<ScreenState<UserAddress?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi()
            .updateUserAddress(token, userId, addressId, userAddress)
            .enqueue(object : Callback<UserAddress> {
                override fun onResponse(
                    call: Call<UserAddress>,
                    response: Response<UserAddress>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(ADDRESS_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<UserAddress>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun deleteUserAddress(
        token: String,
        userId: Long,
        addressId: Long
    ): LiveData<ScreenState<MessageResponse?>> {
        val mutableLiveData = MutableLiveData<ScreenState<MessageResponse?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        RetrofitClient.getInstance().getApi()
            .deleteUserAddress(token, userId, addressId)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(ADDRESS_ERROR, null))
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