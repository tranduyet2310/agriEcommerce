package com.example.argiecommerce.utils

import android.util.Patterns

class Validation {
    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
        private const val NAME_MIN_LENGTH = 3
        private const val PHONE_NUMBER_MIN_LENGHT = 8
        private const val PHONE_NUMBER_MAX_LENGHT = 11

        fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isValidPassword(password: String): Boolean {
            return password.length >= PASSWORD_MIN_LENGTH
        }

        fun isValidName(name: String): Boolean {
            return name.length >= NAME_MIN_LENGTH
        }

        fun isValidPhone(phone: String): Boolean {
            return phone.length in PHONE_NUMBER_MIN_LENGHT..PHONE_NUMBER_MAX_LENGHT
        }
    }
}