package com.example.argiecommerce.view.supplier

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.StandardViewpagerAdapter
import com.example.argiecommerce.databinding.FragmentSuppilerBinding
import com.example.argiecommerce.model.ReviewStatisticResponse
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.ReviewViewModel
import com.example.argiecommerce.viewmodel.SupplierViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator


class SupplierFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSuppilerBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }
    private val supplierViewModel: SupplierViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SupplierViewModel::class.java)
    }
    private val reviewViewModel: ReviewViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ReviewViewModel::class.java)
    }

    private var user: User? = null
    private lateinit var alertDialog: AlertDialog
    val args: SupplierFragmentArgs by navArgs()
    private lateinit var supplierBasicInfo: SupplierBasicInfo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuppilerBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.supplier)

        user = userViewModel.user
        supplierBasicInfo = args.supplier
        setupView()
        getAverageRating()

        return binding.root
    }

    private fun getAverageRating() {
        reviewViewModel.supplierAverageRating(supplierBasicInfo.supplierId).observe(
            requireActivity(), { state -> processAverageRating(state) }
        )
    }

    private fun setupView() {
        val supplierRating = if (supplierBasicInfo.rating == 0.0) getString(R.string._5_0) else supplierBasicInfo.rating.toString()
        binding.suppilerLayout.tvSuppilerName.text = supplierBasicInfo.supplierShopName
        binding.suppilerLayout.tvSuppilerProvince.text = supplierBasicInfo.supplierProvince
        binding.suppilerLayout.tvTotalRatingSuppiler.text = supplierRating
        if (supplierBasicInfo.imageUrl != null) {
            val modifiedUrl = supplierBasicInfo.imageUrl.replace("http://", "https://")
            GlideApp.with(requireContext())
                .load(modifiedUrl)
                .into(binding.suppilerLayout.imageOfSuppiler)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.tvSearch.setOnClickListener(this)
        binding.suppilerLayout.imgSuppilerContact.setOnClickListener(this)
        binding.suppilerLayout.tvSuppilerName.setOnClickListener(this)
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        val specialtiesFragment = arrayListOf(
            SpIntroFragment(),
            SpProductFragment(),
            SpCategoryFragment(),
            SpGardenFragment(),
            SpShopFragment()
        )

        binding.suppilerLayout.viewpagerSuppiler.isUserInputEnabled = false

        val viewPager2Adapter =
            StandardViewpagerAdapter(specialtiesFragment, childFragmentManager, lifecycle)
        binding.suppilerLayout.viewpagerSuppiler.adapter = viewPager2Adapter
        TabLayoutMediator(
            binding.suppilerLayout.tabLayout,
            binding.suppilerLayout.viewpagerSuppiler
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.introduction)
                1 -> tab.text = getString(R.string.product)
                2 -> tab.text = getString(R.string.product_category)
                3 -> tab.text = getString(R.string.garden)
                4 -> tab.text = getString(R.string.shop)
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvSearch -> goToSearchFragment()
            R.id.imgSuppilerContact -> openRegisterDialog()
            R.id.tvSuppilerName -> openInfoDialog()
        }
    }

    private fun openInfoDialog() {
        if (user == null) {
            val dialog = ProgressDialog.createMessageDialog(
                requireContext(),
                requireContext().resources.getString(R.string.need_to_login)
            )
            dialog.show()
        } else {
            navController.navigate(R.id.action_suppilerFragment_to_cropsDialogFragment)
        }
    }

    private fun openRegisterDialog() {
        if (user == null) {
            val dialog = ProgressDialog.createMessageDialog(
                requireContext(),
                requireContext().resources.getString(R.string.need_to_login)
            )
            dialog.show()
        } else {
            navController.navigate(R.id.action_suppilerFragment_to_contactDialogFragment)
        }
    }

    private fun goToSearchFragment() {
        navController.navigate(R.id.action_suppilerFragment_to_searchFragment)
    }

    private fun processAverageRating(state: ScreenState<ReviewStatisticResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    val rating = state.data.averageRating.toDouble()
                    if (rating == 0.0) {
                        binding.suppilerLayout.tvTotalRatingSuppiler.text = getString(R.string._5_0)
                    } else {
                        binding.suppilerLayout.tvTotalRatingSuppiler.text = rating.toString()
                    }
                    supplierBasicInfo.rating = rating
                    userViewModel.supplierBasicInfo = supplierBasicInfo
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
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