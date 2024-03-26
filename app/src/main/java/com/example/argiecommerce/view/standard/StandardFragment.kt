package com.example.argiecommerce.view.standard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.StandardViewpagerAdapter
import com.example.argiecommerce.databinding.FragmentStandardBinding
import com.google.android.material.tabs.TabLayoutMediator


class StandardFragment : Fragment() {
    private var _binding: FragmentStandardBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStandardBinding.inflate(inflater, container, false)
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

}