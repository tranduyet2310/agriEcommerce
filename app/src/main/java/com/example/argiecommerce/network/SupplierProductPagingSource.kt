package com.example.argiecommerce.network

import androidx.paging.PagingSource
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.utils.Constants.DEFAULT_SORT_BY
import com.example.argiecommerce.utils.Constants.DEFAULT_SORT_DIRECTION
import retrofit2.HttpException
import java.io.IOException

class SupplierProductPagingSource(
    private val apiService: Api,
    private val supplierId: Long
) : PagingSource<Int, Product>() {

    override val keyReuseSupported: Boolean = true
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val nextPageNumber = params.key ?: 0

            val response = apiService.getProductBySupplierId(
                supplierId,
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