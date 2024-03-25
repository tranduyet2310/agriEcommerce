package com.example.argiecommerce.network

import androidx.paging.PagingSource
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiRequest
import retrofit2.HttpException
import java.io.IOException

class DiscountProductPagingSource(
    private val apiService: Api,
    private val productApiRequest: ProductApiRequest
) : PagingSource<Int, Product>() {

    override val keyReuseSupported: Boolean = true
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val nextPageNumber = params.key ?: 0

            val sortBy = productApiRequest.sortBy
            val sortDir = productApiRequest.sortDir

            val response = apiService.getProductsWithDiscount(nextPageNumber.toString(), sortBy, sortDir)
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