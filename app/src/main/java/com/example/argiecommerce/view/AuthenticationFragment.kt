package com.example.argiecommerce.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentAuthenticationBinding

class AuthenticationFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.btnContinue.setOnClickListener(this)
        binding.reSend.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnContinue -> goToChangePasswordFragment()
            R.id.reSend -> resendOtpCode()
        }
    }

    private fun resendOtpCode() {

    }

    private fun goToChangePasswordFragment() {

    }


}