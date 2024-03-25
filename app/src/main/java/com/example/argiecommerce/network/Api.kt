package com.example.argiecommerce.network

import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.LoginRequest
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiResponse
import com.example.argiecommerce.model.RegisterApiResponse
import com.example.argiecommerce.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @POST("/api/auth/register/user")
    fun createUser(@Body user: User): Call<RegisterApiResponse>

    @POST("/api/auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginApiResponse>

    @GET("/api/categories")
    fun getAllCategories(): Call<ArrayList<CategoryApiResponse>>

    @GET("/api/products/")
    suspend fun getProductByCategoryId(
        @Query("id") id: Long,
        @Query("pageNo") pageNo: String,
        @Query("sortBy") sortBy: String,
        @Query("sortDir") sortDir: String
    ): Response<ProductApiResponse>
}