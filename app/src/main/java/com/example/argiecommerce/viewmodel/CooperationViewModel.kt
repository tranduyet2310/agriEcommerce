package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.CooperationResponse
import com.example.argiecommerce.model.FavoriteResponse
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.repository.CooperationRepository
import com.example.argiecommerce.utils.ScreenState

class CooperationViewModel(
    private val cooperationRepository: CooperationRepository = CooperationRepository()
) : ViewModel() {
    fun getCooperationByUserId(userId: Long): LiveData<ScreenState<ArrayList<CooperationResponse>?>> {
        return cooperationRepository.getCooperationByUserId(userId)
    }

    fun getCooperationBySupplierId(supplierId: Long): LiveData<ScreenState<ArrayList<CooperationResponse>?>> {
        return cooperationRepository.getCooperationBySupplierId(supplierId)
    }

    fun createCooperation(
        token: String,
        supplierId: Long,
        cooperationResponse: CooperationResponse
    ): LiveData<ScreenState<CooperationResponse?>> {
        return cooperationRepository.createCooperation(token, supplierId, cooperationResponse)
    }

    fun updateCooperation(
        token: String,
        cooperationId: Long,
        cooperationResponse: CooperationResponse
    ): LiveData<ScreenState<CooperationResponse?>> {
        return cooperationRepository.updateCooperation(token, cooperationId, cooperationResponse)
    }

    fun updateCooperationStatus(
        token: String,
        cooperationId: Long,
        cooperationResponse: CooperationResponse
    ): LiveData<ScreenState<CooperationResponse?>> {
        return cooperationRepository.updateCooperationStatus(
            token,
            cooperationId,
            cooperationResponse
        )
    }

    fun updateCooperationAddress(
        token: String,
        cooperationId: Long,
        addressId: Long
    ): LiveData<ScreenState<CooperationResponse?>> {
        return cooperationRepository.updateCooperationAddress(token, cooperationId, addressId)
    }

    fun getCooperationById(cooperationId: Long): LiveData<ScreenState<CooperationResponse?>> {
        return cooperationRepository.getCooperationById(cooperationId)
    }
}