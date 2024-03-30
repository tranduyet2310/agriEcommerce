package com.example.argiecommerce.viewmodel

import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress

class UserViewModel: ViewModel() {
    var user: User? = null
    var userAddress: UserAddress? = null
}