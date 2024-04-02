package com.example.argiecommerce.view.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.StandardViewpagerAdapter
import com.example.argiecommerce.databinding.FragmentSuppilerBinding
import com.google.android.material.tabs.TabLayoutMediator


class SupplierFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSuppilerBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuppilerBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.supplier)

        return binding.root
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

        val specialtiesFragment = arrayListOf<Fragment>(
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
        navController.navigate(R.id.action_suppilerFragment_to_cropsDialogFragment)
    }

    private fun openRegisterDialog() {
        navController.navigate(R.id.action_suppilerFragment_to_contactDialogFragment)
    }

    private fun goToSearchFragment() {
        navController.navigate(R.id.action_suppilerFragment_to_searchFragment)
    }
}