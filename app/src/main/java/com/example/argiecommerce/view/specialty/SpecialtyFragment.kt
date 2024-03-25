package com.example.argiecommerce.view.specialty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.adapter.SpecialtyViewpagerAdapter
import com.example.argiecommerce.databinding.FragmentSpecialtyBinding
import com.google.android.material.tabs.TabLayoutMediator

class SpecialtyFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentSpecialtyBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecialtyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

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
                0 -> tab.text = "Tây Bắc Bộ"
                1 -> tab.text = "Đông Bắc Bộ"
                2 -> tab.text = "ĐB Sông Hồng"
                3 -> tab.text = "Bắc Trung Bộ"
                4 -> tab.text = "DH Nam Trung Bộ"
                5 -> tab.text = "Tây Nguyên"
                6 -> tab.text = "Đông Nam Bộ"
                7 -> tab.text = "ĐB Sông Cửu Long"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

}