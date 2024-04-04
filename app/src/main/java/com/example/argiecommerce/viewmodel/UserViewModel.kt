package com.example.argiecommerce.viewmodel

import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.utils.Constants.PAYMENT_COD
import com.example.argiecommerce.utils.Constants.UNPAID

class UserViewModel: ViewModel() {
    var user: User? = null
    var userAddress: UserAddress? = null
    var total: Long = 0L
    var paymentMethod: String = PAYMENT_COD
    var paymentStatus: String = UNPAID
    var cartProductList: ArrayList<CartResponse> = arrayListOf()
    var orderCreated: Boolean = false
}