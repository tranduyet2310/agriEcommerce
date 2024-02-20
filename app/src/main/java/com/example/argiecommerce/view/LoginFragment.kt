package com.example.argiecommerce.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentLoginBinding


class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.buttonLogin.setOnClickListener(this);
        binding.textViewSignUp.setOnClickListener(this);
        binding.forgetPassword.setOnClickListener(this);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonLogin -> loginUser()
            R.id.textViewSignUp -> goToSignUp()
            R.id.forgetPassword -> goToPasswordAssistant()
        }
    }

    private fun goToSignUp() {
        navController.navigate(R.id.action_loginFragment_to_signUpFragment)
    }

    private fun loginUser() {
        Toast.makeText(requireContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
        navController.navigate(R.id.action_loginFragment_to_homeFragment)
    }

    private fun goToPasswordAssistant(){
        navController.navigate(R.id.action_loginFragment_to_passwordAssistantFragment)
    }
}