package com.example.argiecommerce.network

import androidx.paging.PagingSource
import com.example.argiecommerce.model.ReviewResponse
import com.example.argiecommerce.utils.Constants.DEFAULT_SORT_BY
import com.example.argiecommerce.utils.Constants.DEFAULT_SORT_DIRECTION
import retrofit2.HttpException
import java.io.IOException

class ReviewPagingSource(
    private val apiService: Api,
    private val productId: Long
) : PagingSource<Int, ReviewResponse>() {

    override val keyReuseSupported: Boolean = true
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewResponse> {
        return try {
            val nextPageNumber = params.key ?: 0

            val response = apiService.getAllReviewsByProductId(
                productId,
                nextPageNumber.toString(),
                DEFAULT_SORT_BY,
                DEFAULT_SORT_DIRECTION
            )

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