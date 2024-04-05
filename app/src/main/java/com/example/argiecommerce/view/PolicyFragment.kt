package com.example.argiecommerce.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentPolicyBinding

class PolicyFragment : Fragment() {
    private lateinit var binding: FragmentPolicyBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPolicyBinding.inflate(inflater)

        binding.toolbarLayout.titleToolbar.text = getString(R.string.policy_title)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

}