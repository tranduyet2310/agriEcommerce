package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.LoginRequest
import com.example.argiecommerce.repository.LoginRepository
import com.example.argiecommerce.utils.ScreenState

class LoginViewModel(
    private val repository: LoginRepository = LoginRepository()
) : ViewModel() {

    fun getLoginResponseLiveData(loginRequest: LoginRequest): LiveData<ScreenState<LoginApiResponse?>> {
        return repository.getLoginResponseData(loginRequest);
    }
}