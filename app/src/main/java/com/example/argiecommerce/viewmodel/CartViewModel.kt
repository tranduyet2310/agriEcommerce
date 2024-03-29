package com.example.argiecommerce.viewmodel

import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.CartProduct

class CartViewModel: ViewModel() {
    lateinit var products: ArrayList<CartProduct>
}