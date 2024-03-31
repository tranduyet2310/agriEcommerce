package com.example.argiecommerce.network

import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.LoginRequest
import com.example.argiecommerce.model.PasswordRequest
import com.example.argiecommerce.model.ProductApiResponse
import com.example.argiecommerce.model.RegisterApiResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.model.UserApiResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @POST("/api/auth/register/user")
    fun createUser(@Body user: User): Call<RegisterApiResponse>

    @POST("/api/auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginApiResponse>

    @GET("/api/categories")
    fun getAllCategories(): Call<ArrayList<CategoryApiResponse>>

    @GET("api/users/{id}")
    fun getUserInfo(@Path("id") userId: Long): Call<UserApiResponse>

    @Multipart
    @PATCH("api/users/{id}/avatar")
    fun uploadAvatar(
        @Header("Authorization") token: String,
        @Path("id") userId: Long,
        @Part file: MultipartBody.Part
    ): Call<UserApiResponse>

    @PATCH("api/users/{id}")
    fun updateBasicInfo(
        @Header("Authorization") token: String,
        @Path("id") userId: Long,
        @Body userDto: UserApiResponse
    ): Call<UserApiResponse>

    @PATCH("api/users/{id}/password")
    fun changePassword(
        @Header("Authorization") token: String,
        @Path("id") userId: Long,
        @Body password: PasswordRequest
    ): Call<UserApiResponse>

    @POST("/api/users/{userId}/addresses")
    fun createNewAddress(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Body userAddress: UserAddress
    ): Call<UserAddress>

    @GET("/api/users/{userId}/addresses")
    fun getAddressByUserId(@Path("userId") userId: Long): Call<ArrayList<UserAddress>>

    @PUT("/api/users/{userId}/addresses/{id}")
    fun updateUserAddress(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Path("id") addressId: Long,
        @Body userAddress: UserAddress
    ): Call<UserAddress>

    @DELETE("/api/users/{userId}/addresses/{id}")
    fun deleteUserAddress(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Path("id") addressId: Long
    ): Call<String>

    @GET("/api/products/category")
    suspend fun getProductByCategoryId(
        @Query("id") id: Long,
        @Query("pageNo") pageNo: String,
        @Query("sortBy") sortBy: String,
        @Query("sortDir") sortDir: String
    ): Response<ProductApiResponse>

    @GET("/api/products/subcategory")
    suspend fun getProductBySubcategoryId(
        @Query("id") id: Long,
        @Query("pageNo") pageNo: String,
        @Query("sortBy") sortBy: String,
        @Query("sortDir") sortDir: String
    ): Response<ProductApiResponse>

    @GET("/api/products/discount")
    suspend fun getProductsWithDiscount(
        @Query("pageNo") pageNo: String,
        @Query("sortBy") sortBy: String,
        @Query("sortDir") sortDir: String
    ): Response<ProductApiResponse>

    @GET("/api/products/upcoming")
    suspend fun getUpcomingProducts(
        @Query("pageNo") pageNo: String,
        @Query("sortBy") sortBy: String,
        @Query("sortDir") sortDir: String
    ): Response<ProductApiResponse>

    @GET("/api/products/search")
    suspend fun searchProduct(
        @Query("query") query: String,
        @Query("pageNo") pageNo: String,
        @Query("sortBy") sortBy: String,
        @Query("sortDir") sortDir: String
    ): Response<ProductApiResponse>
}