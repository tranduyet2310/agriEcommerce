package com.example.argiecommerce.view.loginRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentPasswordAssistantBinding
import com.example.argiecommerce.utils.Validation
import com.example.argiecommerce.viewmodel.UserViewModel

class PasswordAssistantFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPasswordAssistantBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordAssistantBinding.inflate(inflater, container, false)

        binding.toolbarLayout.titleToolbar.text = getString(R.string.forgot_pass)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.btnContinue.setOnClickListener(this)
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnContinue -> goToAuthenticationFragment()
        }
    }

    private fun goToAuthenticationFragment() {
        val email = binding.emailAddress.text.toString()
        if (email.isEmpty()){
            binding.emailAddress.setError("Yêu cầu nhập email")
            binding.emailAddress.requestFocus()
            return
        }

        if (!Validation.isValidEmail(email)){
            binding.emailAddress.setError("Email không đúng định dạng")
            binding.emailAddress.requestFocus()
            return
        }


        navController.navigate(R.id.action_passwordAssistantFragment_to_authenticationFragment)
    }

}