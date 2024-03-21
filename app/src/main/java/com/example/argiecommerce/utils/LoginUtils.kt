package com.example.argiecommerce.utils

import android.content.Context
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.Constants.AVATAR
import com.example.argiecommerce.utils.Constants.EMAIL
import com.example.argiecommerce.utils.Constants.ID
import com.example.argiecommerce.utils.Constants.NAME
import com.example.argiecommerce.utils.Constants.PASSWORD
import com.example.argiecommerce.utils.Constants.PHONE
import com.example.argiecommerce.utils.Constants.SHARED_PREF_NAME
import com.example.argiecommerce.utils.Constants.TOKEN

class LoginUtils(private val mCtx: Context) {
//    companion object {
//        private var mInstance: LoginUtils? = null
//
//        @Synchronized
//        fun getInstance(mCtx: Context): LoginUtils {
//            if (mInstance == null) {
//                mInstance = LoginUtils(mCtx)
//            }
//            return mInstance as LoginUtils
//        }
//    }

    fun saveUserInfo(response: LoginApiResponse) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt(ID, response.getId())
        editor.putString(EMAIL, response.getEmail())
        editor.putString(PASSWORD, response.getPassword())
        editor.putString(TOKEN, response.getAccessToken())
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt("id", -1) != -1
    }

    fun saveUserInfo(user: User) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt(ID, user.getId())
        editor.putString(NAME, user.getFullName())
        editor.putString(EMAIL, user.getEmail())
        editor.putString(PASSWORD, user.getPassword())
        editor.putString(PHONE, user.getPhone())
        editor.putString(AVATAR, user.getAvatar())
        editor.apply()
    }

    fun getUserInfo(): User {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return User(
            sharedPreferences.getInt(ID, -1),
            sharedPreferences.getString(NAME, null),
            sharedPreferences.getString(PHONE, null),
            sharedPreferences.getString(EMAIL, null),
            sharedPreferences.getString(PASSWORD, null),
            sharedPreferences.getString(AVATAR, "")
        )
    }

    fun getUserToken(): String {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN, "") ?: ""
    }

    fun clearAll() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear().apply()
    }

}