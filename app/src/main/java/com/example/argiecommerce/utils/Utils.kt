package com.example.argiecommerce.utils

import android.content.Context
import java.io.InputStream
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

        fun formatYield(yield: Double): String {
            val convertValue: Double
            val yieldText: String

            if (yield >= 1000) {
                convertValue = yield / 1000
                yieldText = "${convertValue} " + Constants.TAN_UNIT
            } else if (yield >= 100) {
                convertValue = yield / 100
                yieldText = "${convertValue} " + Constants.TA_UNIT
            } else if (yield >= 10) {
                convertValue = yield / 10
                yieldText = "${convertValue} " + Constants.YEN_UNIT
            } else {
                yieldText = "${yield} " + Constants.KG_UNIT
            }
            return yieldText;
        }

        lateinit var certsInputStream: InputStream
        fun readRawResource(context: Context, resId: Int) {
            certsInputStream = context.resources.openRawResource(resId)
        }

        fun convertToLong(input: String): Long {
            val result = input.replace(".","").replace(",","")
            return result.toLong()
        }

        fun getExpireDate(): String {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val dateAfter7Days = calendar.time
            return dateFormat.format(dateAfter7Days )
        }
    }
}