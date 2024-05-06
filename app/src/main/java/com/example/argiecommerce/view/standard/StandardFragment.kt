package com.example.argiecommerce.view.standard

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.StandardViewpagerAdapter
import com.example.argiecommerce.databinding.FragmentStandardBinding
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.network.Api
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StandardFragment : Fragment() {
    private var _binding: FragmentStandardBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val apiService: Api by lazy {
        RetrofitClient.getInstance().getApi()
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }

    private var specialtyCategory: CategoryApiResponse? = null
    private lateinit var alertDialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStandardBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }
            getCategoryData()
            withContext(Dispatchers.Main) {
                alertDialog.dismiss()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val specialtiesFragment = arrayListOf<Fragment>(
            OcopFragment(),
            VietGapFragment(),
            GlobalGapFragment(),
            OrganicFragment()
        )

        binding.viewpagerStandard.isUserInputEnabled = false

        val viewPager2Adapter =
            StandardViewpagerAdapter(specialtiesFragment, childFragmentManager, lifecycle)
        binding.viewpagerStandard.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerStandard) { tab, position ->
            when (position) {
                0 -> tab.text = requireContext().resources.getString(R.string.ocop)
                1 -> tab.text = requireContext().resources.getString(R.string.vietgap)
                2 -> tab.text = requireContext().resources.getString(R.string.globalg_a_p)
                3 -> tab.text = requireContext().resources.getString(R.string.organic)
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    suspend fun getCategoryData() {
        withContext(Dispatchers.IO) {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val categoryList = response.body()
                    if (!categoryList!!.isEmpty()) {
                        for (category in categoryList) {
                            if (category.categoryName.equals(Constants.STANDARD)) {
                                specialtyCategory = category
                                userViewModel.category = specialtyCategory
                            }
                        }
                    }
                }
            }
        }
    }

}