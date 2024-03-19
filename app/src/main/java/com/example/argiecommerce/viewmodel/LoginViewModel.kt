package com.example.argiecommerce.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.LoginRequest
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.repository.LoginRepository
import com.example.argiecommerce.utils.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val repository: LoginRepository = LoginRepository()
) : ViewModel() {

    fun getLoginResponseLiveData(loginRequest: LoginRequest): LiveData<ScreenState<LoginApiResponse?>> {
        return repository.getLoginResponseData(loginRequest);
    }
}