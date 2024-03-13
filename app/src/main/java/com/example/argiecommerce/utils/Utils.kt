package com.example.argiecommerce.utils

import java.text.DecimalFormat

class Utils {
    object getText{
        fun getCategoryItemTitle(): Array<String>{
            return arrayOf(
                "Đồ uống", "Rau củ", "Hoa quả", "Gia vị",
                "Đặc sản", "Thực phẩm khô", "Tiêu chuẩn","VietGAP", "GlobalG.A.P", "OCOP"
            )
        }
    }
}