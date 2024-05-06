package com.example.argiecommerce.view.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CategoryAdapter
import com.example.argiecommerce.adapter.SubcategoryAdapter
import com.example.argiecommerce.databinding.FragmentSuppilerCategoryBinding
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.Subcategory
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.SupplierViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class SpCategoryFragment : Fragment() {
    private lateinit var binding: FragmentSuppilerCategoryBinding

    private lateinit var subcategoryAdapter: SubcategoryAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryDetailList: ArrayList<Subcategory>
    private lateinit var categoryList: ArrayList<CategoryApiResponse>

    private val supplierViewModel: SupplierViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SupplierViewModel::class.java)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private var supplierBasicInfo: SupplierBasicInfo? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuppilerCategoryBinding.inflate(inflater)

        supplierBasicInfo = userViewModel.supplierBasicInfo
        setupRecyclerView()
        getData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAdapter.onClick = {
            val subcategoryList = it.subCategoryList

            categoryDetailList.clear()
            subcategoryList.forEach {
                categoryDetailList.add(it)
            }
            binding.rcvSupplierCategory.adapter?.notifyDataSetChanged()
        }
    }

    private fun getData() {
        supplierViewModel.getCategoryBySupplierId(supplierBasicInfo!!.supplierId).observe(
            requireActivity(), { state -> processCategoryResponse(state) }
        )
    }

    private fun setupRecyclerView() {
        categoryDetailList = arrayListOf()
        subcategoryAdapter = SubcategoryAdapter(categoryDetailList)

        binding.rcvSupplierCategory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = subcategoryAdapter
        }

        categoryList = arrayListOf()
        categoryAdapter = CategoryAdapter(requireContext(), categoryList)

        binding.rcvCategory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = categoryAdapter
        }
    }

    private fun processCategoryResponse(state: ScreenState<ArrayList<CategoryApiResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    binding.progressBar.visibility = View.GONE
                    if (state.data.size == 0){
                        showSnackbar(getString(R.string.shop_no_product))
                    } else {
                        categoryList.clear()
                        categoryList.addAll(state.data)
                        binding.rcvCategory.adapter?.notifyDataSetChanged()
                    }
                }
            }

            is ScreenState.Error -> {
                binding.progressBar.visibility = View.GONE
                if (state.message != null) {
                    showSnackbar(state.message)
                }
            }
        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
    }
}