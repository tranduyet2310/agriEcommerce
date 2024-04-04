package com.example.argiecommerce.network

import android.util.Log
import androidx.paging.PagingSource
import com.example.argiecommerce.model.OrderResponse
import retrofit2.HttpException
import java.io.IOException

class OrderPagingSource(
    private val apiService: Api,
    private val userId: Long
) : PagingSource<Int, OrderResponse>() {

    override val keyReuseSupported: Boolean = true
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderResponse> {
        return try {
            val nextPageNumber = params.key ?: 0

            val response = apiService.getOrderByUserId(userId, nextPageNumber.toString(), "dateCreated", "desc")
            val data = response.body()?.content

            LoadResult.Page(
                data ?: arrayListOf(),
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = if (nextPageNumber < response.body()!!.totalPage) nextPageNumber + 1 else null
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}