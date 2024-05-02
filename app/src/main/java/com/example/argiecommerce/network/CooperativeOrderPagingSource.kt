package com.example.argiecommerce.network

import androidx.paging.PagingSource
import com.example.argiecommerce.model.CooperativePayment
import retrofit2.HttpException
import java.io.IOException

class CooperativeOrderPagingSource (
    private val apiService: Api,
    private val userId: Long
) : PagingSource<Int, CooperativePayment>() {

    override val keyReuseSupported: Boolean = true
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CooperativePayment> {
        return try {
            val nextPageNumber = params.key ?: 0

            val response = apiService.getCooperativeOrderByUserId(userId, nextPageNumber.toString(), "dateCreated", "desc")
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