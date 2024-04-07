package com.example.argiecommerce.view.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentSpShopBinding
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.network.Api
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SpShopFragment : Fragment() {
    private lateinit var binding: FragmentSpShopBinding
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val apiService: Api by lazy {
        RetrofitClient.getInstance().getApi()
    }
    private var supplierBasicInfo: SupplierBasicInfo? = null
    private lateinit var totalProduct: String
    private lateinit var totalReviews: String
    private lateinit var totalSoldProduct: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpShopBinding.inflate(inflater, container, false)

        supplierBasicInfo = userViewModel.supplierBasicInfo
        if (supplierBasicInfo != null) {
            val rating: String =
                if (supplierBasicInfo!!.rating == 0.0) getString(R.string._5_0) else supplierBasicInfo!!.rating.toString()
            binding.tvSuppilerRating.text = rating

            lifecycleScope.launch(Dispatchers.IO) {
                 totalProduct = getTotalProductBySupplier()
                 totalReviews = getSupplierTotalReview()
                 totalSoldProduct = getSoldProductBySupplier()

                withContext(Dispatchers.Main){
                    binding.tvSuppilerProduct.text = totalProduct
                    binding.tvSuppilerTime.text = totalReviews
                    binding.tvSuppilerNote.text = totalSoldProduct
                }
            }
        }

        return binding.root
    }

    suspend fun getSupplierTotalReview(): String {
        return withContext(Dispatchers.IO) {
            val response = apiService.getSupplierTotalReview(supplierBasicInfo!!.supplierId)
            if (response.isSuccessful) {
                val result = response.body()?.message
                result ?: "0"
            } else {
                "0"
            }
        }
    }

    suspend fun getTotalProductBySupplier(): String {
        return withContext(Dispatchers.IO) {
            val response = apiService.getTotalProductBySupplier(supplierBasicInfo!!.supplierId)
            if (response.isSuccessful) {
                val result = response.body()?.message
                result ?: "0"
            } else {
                "0"
            }
        }
    }

    suspend fun getSoldProductBySupplier(): String {
        return withContext(Dispatchers.IO) {
            val response = apiService.getSoldProductBySupplier(supplierBasicInfo!!.supplierId)
            if (response.isSuccessful) {
                val result = response.body()?.message
                result ?: "0"
            } else {
                "0"
            }
        }
    }
}