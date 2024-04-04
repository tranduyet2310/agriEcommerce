package com.example.argiecommerce.network

import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.CurrencyResponse
import com.example.argiecommerce.model.FavoriteResponse
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.LoginRequest
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.model.OrderApiResponse
import com.example.argiecommerce.model.OrderDetailResponse
import com.example.argiecommerce.model.OrderRequest
import com.example.argiecommerce.model.OrderResponse
import com.example.argiecommerce.model.PasswordRequest
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiResponse
import com.example.argiecommerce.model.RegisterApiResponse
import com.example.argiecommerce.model.ReviewApiResponse
import com.example.argiecommerce.model.ReviewRequest
import com.example.argiecommerce.model.ReviewResponse
import com.example.argiecommerce.model.ReviewStatisticResponse
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

    @GET("api/favorites/{userId}/list")
    fun getFavoriteProducts(@Path("userId") userId: Long): Call<ArrayList<FavoriteResponse>>

    @DELETE("/api/favorites/{userId}/{productId}")
    fun deleteFavoriteProduct(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Path("productId") productId: Long,
    ): Call<MessageResponse>

    @POST("/api/favorites/{userId}/add/{productId}")
    fun createFavProduct(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Path("productId") productId: Long,
    ): Call<FavoriteResponse>

    @POST("/api/cart/{userId}/{productId}")
    fun addToCart(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Path("productId") productId: Long,
    ): Call<CartResponse>

    @GET("api/cart/{userId}")
    fun getAllCartItems(@Path("userId") userId: Long): Call<ArrayList<CartResponse>>

    @PATCH("/api/cart")
    fun changeQuantity(
        @Header("Authorization") token: String,
        @Query("userId") userId: Long,
        @Query("productId") productId: Long,
        @Query("quantity") quantity: Int
    ): Call<CartResponse>

    @DELETE("/api/cart/{userId}/{productId}")
    fun removeFromCart(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Path("productId") productId: Long
    ): Call<MessageResponse>

    @DELETE("/api/cart/{userId}")
    fun deleteAllItemsByUserId(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long
    ): Call<MessageResponse>

    @POST("/api/orders/{userId}")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Body orderRequest: OrderRequest
    ): Response<OrderResponse>

    @GET("/api/orders/{userId}")
    suspend fun getOrderByUserId(
        @Path("userId") userId: Long,
        @Query("pageNo") pageNo: String,
        @Query("sortBy") sortBy: String,
        @Query("sortDir") sortDir: String
    ): Response<OrderApiResponse>

    @POST("/api/details/{orderId}")
    suspend fun createOrderDetail(
        @Header("Authorization") token: String,
        @Path("orderId") orderId: Long,
        @Body orderDetailResponse: OrderDetailResponse
    ): Response<OrderDetailResponse>

    @PATCH("/api/products/{productId}")
    suspend fun increaseSoldProduct(
        @Header("Authorization") token: String,
        @Path("productId") productId: Long,
        @Query("quantity") quantity: Long
    ): Response<Product>

    @GET("/api/details/{orderId}")
    fun getDetailsByOrderId(
        @Path("orderId") orderId: Long
    ): Call<ArrayList<OrderDetailResponse>>

    @PATCH("/api/orders/{orderId}")
    fun updateOrderStatus(
        @Header("Authorization") token: String,
        @Path("orderId") orderId: Long,
        @Query("orderStatus") orderStatus: String
    ): Call<OrderResponse>

    @GET("/api/orders/{userId}/{productId}")
    fun checkUserPurchasedOrNot(
        @Path("userId") userId: Long,
        @Path("productId") productId: Long
    ): Call<MessageResponse>

    @POST("api/reviews/{userId}/{productId}")
    fun createReview(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Path("productId") productId: Long,
        @Body reviewRequest: ReviewRequest
    ): Call<ReviewResponse>

    @GET("api/reviews/{productId}/list")
    suspend fun getAllReviewsByProductId(
        @Path("productId") productId: Long,
        @Query("pageNo") pageNo: String,
        @Query("sortBy") sortBy: String,
        @Query("sortDir") sortDir: String
    ): Response<ReviewApiResponse>

    @GET("api/reviews/{productId}/total")
    fun calculateTotalRating(@Path("productId") productId: Long): Call<ReviewStatisticResponse>

    @GET("api/reviews/{productId}/statistic")
    fun statisticRating(@Path("productId") productId: Long): Call<ReviewStatisticResponse>

    @GET("api/reviews/{productId}/average")
    fun averageRating(@Path("productId") productId: Long): Call<ReviewStatisticResponse>

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

    @GET("/api/users/addresses/{id}")
    fun getAddressById(@Path("id") addressId: Long): Call<UserAddress>

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
    ): Call<MessageResponse>

    @GET("https://api.getgeoapi.com/v2/currency/convert")
    suspend fun convertCurrency(
        @Query("api_key") key: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double,
        @Query("format") format: String,
    ): Response<CurrencyResponse>

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