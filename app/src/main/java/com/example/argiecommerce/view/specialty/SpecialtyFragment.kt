package com.example.argiecommerce.view.specialty

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.SpecialtyViewpagerAdapter
import com.example.argiecommerce.databinding.FragmentSpecialtyBinding
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.network.Api
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpecialtyFragment : Fragment() {
    private var _binding: FragmentSpecialtyBinding? = null
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
        _binding = FragmentSpecialtyBinding.inflate(inflater, container, false)

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

        setupViewPager()
    }

    private fun setupViewPager() {
        val specialtiesFragment = arrayListOf<Fragment>(
            TayBacBoFragment(),
            DongBacBoFragment(),
            DbSongHongFragment(),
            BacTrungBoFragment(),
            DhNamTrungBoFragment(),
            TayNguyenFragment(),
            DongNamBoFragment(),
            DbSongCuuLongFragment()
        )

        binding.viewpagerSpecialty.isUserInputEnabled = false

        val viewPager2Adapter =
            SpecialtyViewpagerAdapter(specialtiesFragment, childFragmentManager, lifecycle)
        binding.viewpagerSpecialty.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerSpecialty) { tab, position ->
            when (position) {
                0 -> tab.text = requireContext().resources.getString(R.string.tay_bac_bo_name)
                1 -> tab.text = requireContext().resources.getString(R.string.dong_bac_bo_name)
                2 -> tab.text = requireContext().resources.getString(R.string.db_song_hong_name)
                3 -> tab.text = requireContext().resources.getString(R.string.bac_trung_bo_name)
                4 -> tab.text = requireContext().resources.getString(R.string.dh_nam_trung_bo_name)
                5 -> tab.text = requireContext().resources.getString(R.string.tay_nguyen_name)
                6 -> tab.text = requireContext().resources.getString(R.string.dong_nam_bo_name)
                7 -> tab.text = requireContext().resources.getString(R.string.db_song_cuu_long_name)
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
                            if (category.categoryName.equals(Constants.SPECIALTY)) {
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