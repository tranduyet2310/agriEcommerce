package com.example.argiecommerce.view.category

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CategoryAdapter
import com.example.argiecommerce.adapter.SubcategoryAdapter
import com.example.argiecommerce.databinding.FragmentCategoryBinding
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.CategoryViewModel
import com.google.android.material.snackbar.Snackbar

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var subcategoryAdapter: SubcategoryAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryDetailList: ArrayList<String>

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CategoryViewModel::class.java)
    }
    private lateinit var alertDialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        setupSubcategoryData()
        getCategoryData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        subcategoryAdapter.onClick = {
            val b = Bundle().apply { putString("category", it) }
            navController.navigate(R.id.action_categoryFragment_to_seeAllFragment, b)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCategoryData() {
        categoryViewModel.getCategoryResponseData()
            .observe(requireActivity(), { state -> processCategoryResponse(state) })
    }

    private fun setupSubcategoryData() {
        categoryDetailList = arrayListOf()
        subcategoryAdapter = SubcategoryAdapter(categoryDetailList)

        binding.rvCategoryDetail.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = subcategoryAdapter
        }
    }

    private fun processCategoryResponse(state: ScreenState<List<CategoryApiResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    categoryAdapter = CategoryAdapter(requireContext(), state.data)
                    binding.rvCategoryItem.apply {
                        layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
                        setHasFixedSize(true)
                        categoryAdapter.onClick = {
                            val subcategoryList = it.subCategoryList
                            if(subcategoryList.isEmpty()){
                                binding.notFoundLayout.visibility = View.VISIBLE
                                binding.rvCategoryDetail.visibility = View.INVISIBLE
                            } else {
                                binding.notFoundLayout.visibility = View.GONE
                                binding.rvCategoryDetail.visibility = View.VISIBLE

                                categoryDetailList.clear()
                                subcategoryList.forEach {
                                    categoryDetailList.add(it.subcategoryName)
                                }
                                binding.rvCategoryDetail.adapter?.notifyDataSetChanged()
                            }
                        }
                        adapter = categoryAdapter
                    }
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    displayErrorSnackbar(state.message)
                }
            }
        }
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction("Thử lại 👍") { dismiss() } }
            .show()
    }
}