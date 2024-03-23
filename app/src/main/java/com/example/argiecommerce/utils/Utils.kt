package com.example.argiecommerce.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class Utils {
    companion object {
        fun Long.formatPrice(): String {
            val symbols = DecimalFormatSymbols().apply {
                groupingSeparator = '.'
                decimalSeparator = ','
            }
            val formatter = DecimalFormat("#,###", symbols)
            return formatter.format(this)
        }

        fun calculateDiscountPercentage(originalPrice: Long, discountedPrice: Long): Long {
            val discountPercentage: Double =
                ((originalPrice - discountedPrice) * 1.0 / originalPrice) * 100
            return Math.round(discountPercentage)
        }
    }
}