package com.example.argiecommerce.network

import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.LoginRequest
import com.example.argiecommerce.model.RegisterApiResponse
import com.example.argiecommerce.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @POST("/api/auth/register/user")
    fun createUser(@Body user: User): Call<RegisterApiResponse>
    @POST("/api/auth/login")
    fun loginUser(@Body loginRequest: LoginRequest) : Call<LoginApiResponse>
}