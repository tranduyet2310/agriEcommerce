package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.RegisterApiResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.repository.RegisterRepository

class RegisterViewModel: ViewModel() {
    private val registerRepository: RegisterRepository = RegisterRepository()

    fun getRegisterResponseLiveData(user: User): LiveData<RegisterApiResponse>{
        return registerRepository.getRegisterResponseData(user)
    }

}