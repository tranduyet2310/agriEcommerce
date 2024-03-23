package com.example.argiecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.repository.CategoryRepository
import com.example.argiecommerce.utils.ScreenState

class CategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {
    fun getCategoryResponseData(): LiveData<ScreenState<ArrayList<CategoryApiResponse>?>> {
        return categoryRepository.getCategoryResponseDate()
    }
}