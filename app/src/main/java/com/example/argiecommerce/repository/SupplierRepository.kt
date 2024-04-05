package com.example.argiecommerce.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.FieldApiResponse
import com.example.argiecommerce.model.Image
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.model.OrderDetailResponse
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.model.SupplierIntroResponse
import com.example.argiecommerce.network.ProductPagingSource
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.network.SupplierProductPagingSource
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.ScreenState
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalPagingApi::class)
class SupplierRepository(context: Context) {
    private val apiService = RetrofitClient.getInstance().getApi()
    fun getSupplierAvatar(
        supplierId: Long
    ): LiveData<ScreenState<Image?>> {
        val mutableLiveData = MutableLiveData<ScreenState<Image?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))
        apiService.getSupplierAvatar(supplierId)
            .enqueue(object : Callback<Image> {
                override fun onResponse(call: Call<Image>, response: Response<Image>) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<Image>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })
        return mutableLiveData
    }

    fun getSupplierIntroInfo(supplierId: Long): LiveData<ScreenState<ArrayList<SupplierIntroResponse>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ArrayList<SupplierIntroResponse>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.getAllSupplierIntro(supplierId)
            .enqueue(object : Callback<ArrayList<SupplierIntroResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<SupplierIntroResponse>>,
                    response: Response<ArrayList<SupplierIntroResponse>>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ArrayList<SupplierIntroResponse>>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun getSupplierProduct(supplierId: Long): Flow<PagingData<Product>> {
        return Pager(
            PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE.toInt(),
                enablePlaceholders = false
            )
        ) {
            SupplierProductPagingSource(apiService, supplierId)
        }.flow
    }

    fun getCategoryBySupplier(supplierId: Long): LiveData<ScreenState<ArrayList<CategoryApiResponse>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ArrayList<CategoryApiResponse>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.getCategoryBySupplierId(supplierId)
            .enqueue(object : Callback<ArrayList<CategoryApiResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<CategoryApiResponse>>,
                    response: Response<ArrayList<CategoryApiResponse>>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ArrayList<CategoryApiResponse>>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }

    fun getFieldInfo(supplierId: Long): LiveData<ScreenState<ArrayList<FieldApiResponse>?>> {
        val mutableLiveData = MutableLiveData<ScreenState<ArrayList<FieldApiResponse>?>>()
        mutableLiveData.postValue(ScreenState.Loading(null))

        apiService.getFieldInfo(supplierId)
            .enqueue(object : Callback<ArrayList<FieldApiResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<FieldApiResponse>>,
                    response: Response<ArrayList<FieldApiResponse>>
                ) {
                    if (response.isSuccessful) {
                        mutableLiveData.postValue(ScreenState.Success(response.body()))
                    } else {
                        mutableLiveData.postValue(ScreenState.Error(Constants.SERVER_ERROR, null))
                    }
                }

                override fun onFailure(call: Call<ArrayList<FieldApiResponse>>, t: Throwable) {
                    val message = t.message.toString()
                    mutableLiveData.postValue(ScreenState.Error(message, null))
                }
            })

        return mutableLiveData
    }
}