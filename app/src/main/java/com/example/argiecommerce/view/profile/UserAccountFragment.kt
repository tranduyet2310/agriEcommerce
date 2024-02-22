package com.example.argiecommerce.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentUserAccountBinding


class UserAccountFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentUserAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.tvUpdatePassword.setOnClickListener(this)
        binding.buttonSave.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvUpdatePassword -> goToChangePasswordFragment()
            R.id.buttonSave -> saveInfoUserAccount()
        }
    }

    private fun saveInfoUserAccount() {
        navController.navigate(R.id.action_userAccountFragment_to_profileFragment)
    }

    private fun goToChangePasswordFragment() {
        navController.navigate(R.id.action_userAccountFragment_to_changePasswordFragment)
    }
}