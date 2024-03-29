package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.repository.UserAddressRepository
import com.example.argiecommerce.utils.ScreenState

class UserAddressViewModel(
    private val userAddressRepository: UserAddressRepository = UserAddressRepository()
) : ViewModel() {
    fun createNewAddress(
        token: String,
        userId: Long,
        userAddress: UserAddress
    ): LiveData<ScreenState<UserAddress?>> {
        return userAddressRepository.createNewUserAddress(token, userId, userAddress)
    }

    fun getAddressByUserId(userId: Long): LiveData<ScreenState<ArrayList<UserAddress>?>> {
        return userAddressRepository.getAddressByUserId(userId)
    }
}