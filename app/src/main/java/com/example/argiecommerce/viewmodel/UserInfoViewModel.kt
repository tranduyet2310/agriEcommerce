package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.PasswordRequest
import com.example.argiecommerce.model.UserApiResponse
import com.example.argiecommerce.repository.UserInfoRepository
import com.example.argiecommerce.utils.ScreenState
import okhttp3.MultipartBody

class UserInfoViewModel(
    private val userInfoRepository: UserInfoRepository = UserInfoRepository()
) : ViewModel() {
    fun getUserInfo(token: String, userId: Long): LiveData<ScreenState<UserApiResponse?>> {
        return userInfoRepository.getUserInfo(token, userId)
    }

    fun changeUserAvatar(
        token: String,
        userId: Long,
        file: MultipartBody.Part
    ): LiveData<ScreenState<UserApiResponse?>> {
        return userInfoRepository.uploadAvatar(token, userId, file)
    }

    fun updateBasicInfo(
        token: String,
        userId: Long,
        userDto: UserApiResponse
    ): LiveData<ScreenState<UserApiResponse?>> {
        return userInfoRepository.updateBasicInfo(token, userId, userDto)
    }

    fun changePassword(
        token: String,
        userId: Long,
        passwordRequest: PasswordRequest
    ): LiveData<ScreenState<UserApiResponse?>> {
        return userInfoRepository.changePassword(token, userId, passwordRequest)
    }
}