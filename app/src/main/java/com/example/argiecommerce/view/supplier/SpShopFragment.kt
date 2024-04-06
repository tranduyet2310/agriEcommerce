package com.example.argiecommerce.view.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentSpShopBinding


class SpShopFragment : Fragment() {
    private lateinit var binding: FragmentSpShopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpShopBinding.inflate(inflater, container, false)



        return binding.root
    }

}