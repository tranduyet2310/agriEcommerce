package com.example.argiecommerce.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.model.ProductApiResponse
import com.example.argiecommerce.network.ProductPagingSource
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.ScreenState
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class ProductRepository {
//     fun getProductByCategory(productApiRequest: ProductApiRequest): LiveData<ScreenState<ProductApiResponse?>> {
//        val mutableLiveData = MutableLiveData<ScreenState<ProductApiResponse?>>()
//        mutableLiveData.postValue(ScreenState.Loading(null))
//
//        val categoryId = productApiRequest.id
//        val pageNo = productApiRequest.pageNo
//        val pageSize = productApiRequest.pageSize
//        val sortBy = productApiRequest.sortBy
//        val sortDir = productApiRequest.sortDir
//
//        RetrofitClient.getInstance().getApi()
//            .getProductByCategoryId(categoryId, pageNo, pageSize, sortBy, sortDir)
//            .enqueue(object : Callback<ProductApiResponse> {
//                override fun onResponse(
//                    call: Call<ProductApiResponse>,
//                    response: Response<ProductApiResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        mutableLiveData.postValue(ScreenState.Success(response.body()))
//                    } else {
//                        val message = "Máy chủ gặp sự cố! 😣"
//                        mutableLiveData.postValue(ScreenState.Error(message, null))
//                    }
//                }
//
//                override fun onFailure(call: Call<ProductApiResponse>, t: Throwable) {
//                    val message = t.message.toString()
//                    mutableLiveData.postValue(ScreenState.Error(message, null))
//                }
//            })
//
//        return mutableLiveData
//    }
//}

@OptIn(ExperimentalPagingApi::class)
class ProductRepository(context: Context) {
    private val apiService = RetrofitClient.getInstance().getApi()

    fun getProductByCategory(productApiRequest: ProductApiRequest): Flow<PagingData<Product>> {
        return Pager(
            PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            )
        ) {
            ProductPagingSource(apiService, productApiRequest)
        }.flow
    }
}