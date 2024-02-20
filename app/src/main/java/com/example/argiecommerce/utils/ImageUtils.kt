package com.example.argiecommerce.utils

import com.example.argiecommerce.R

class ImageUtils {

    object getImages {
        fun getSlides(): List<Int> {
            return listOf<Int>(
                R.drawable.viewfilpper_1,
                R.drawable.viewfilpper_2,
                R.drawable.viewfilpper_3,
                R.drawable.viewfilpper_4,
                R.drawable.viewfilpper_5,
                R.drawable.viewfilpper_6,
                R.drawable.viewfilpper_7,
                R.drawable.viewfilpper_8
            )
        }

        fun getCategoryItem(): Array<Int>{
            return arrayOf(
                R.drawable.drink,
                R.drawable.vegetable,
                R.drawable.fruits,
                R.drawable.spicy_food,
                R.drawable.badge,
                R.drawable.dried_fruit,
                R.drawable.standard,
                R.drawable.vietgap,
                R.drawable.global_gap,
                R.drawable.ocop
            )
        }
    }

}