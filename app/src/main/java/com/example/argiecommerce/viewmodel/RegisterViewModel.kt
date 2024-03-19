package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.RegisterApiResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.repository.RegisterRepository
import com.example.argiecommerce.utils.ScreenState

class RegisterViewModel(
    private val registerRepository: RegisterRepository = RegisterRepository()
) : ViewModel() {

    fun getRegisterResponseLiveData(user: User): LiveData<ScreenState<RegisterApiResponse?>> {
        return registerRepository.getRegisterResponseData(user)
    }
}