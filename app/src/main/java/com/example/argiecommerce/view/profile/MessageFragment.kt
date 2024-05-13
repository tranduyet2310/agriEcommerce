package com.example.argiecommerce.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.SpecialtyViewpagerAdapter
import com.example.argiecommerce.databinding.FragmentMessageBinding
import com.example.argiecommerce.view.specialty.BacTrungBoFragment
import com.example.argiecommerce.view.specialty.DbSongCuuLongFragment
import com.example.argiecommerce.view.specialty.DbSongHongFragment
import com.example.argiecommerce.view.specialty.DhNamTrungBoFragment
import com.example.argiecommerce.view.specialty.DongBacBoFragment
import com.example.argiecommerce.view.specialty.DongNamBoFragment
import com.example.argiecommerce.view.specialty.TayBacBoFragment
import com.example.argiecommerce.view.specialty.TayNguyenFragment
import com.google.android.material.tabs.TabLayoutMediator


class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageBinding.inflate(inflater)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.message)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        setupViewPager()
    }

    private fun setupViewPager() {
        val messagesFragment = arrayListOf(ChatFragment(), SearchChatFragment())
        binding.viewpager.isUserInputEnabled = false
        val viewPager2Adapter = SpecialtyViewpagerAdapter(messagesFragment, childFragmentManager, lifecycle)
        binding.viewpager.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = requireContext().resources.getString(R.string.content)
                1 -> tab.text = requireContext().resources.getString(R.string.search_chat)
            }
        }.attach()
    }

}