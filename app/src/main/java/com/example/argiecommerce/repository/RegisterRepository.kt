package com.example.argiecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.argiecommerce.model.RegisterApiResponse
import com.example.argiecommerce.model.User

class RegisterRepository {
    companion object {
        private const val TAG = "RegisterRepository"
    }

    fun getRegisterResponseData(user: User): LiveData<RegisterApiResponse>{
        val mutableLiveData = MutableLiveData<RegisterApiResponse>()



        return mutableLiveData
    }
}