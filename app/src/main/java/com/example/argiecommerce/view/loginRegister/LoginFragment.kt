package com.example.argiecommerce.view.loginRegister

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentLoginBinding
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.LoginRequest
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
    }

    private lateinit var alertDialog: AlertDialog

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

        binding.textFieldEmail.editText?.addTextChangedListener {
            binding.textFieldEmail.error = null
        }

        binding.textFieldPassword.editText?.addTextChangedListener {
            binding.textFieldPassword.error = null
        }

        binding.buttonLogin.setOnClickListener(this);
        binding.textViewSignUp.setOnClickListener(this);
        binding.forgetPassword.setOnClickListener(this);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonLogin -> loginUser()
            R.id.textViewSignUp -> goToSignUp()
            R.id.forgetPassword -> goToPasswordAssistant()
        }
    }

    private fun goToSignUp() {
        navController.navigate(R.id.action_loginFragment_to_signUpFragment)
    }

    private fun loginUser() {
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()
        if (email.isEmpty()) {
            binding.textFieldEmail.error = "Yêu cầu nhập email"
            return
        }
        if (password.isEmpty()) {
            binding.textFieldPassword.error = "Yêu cầu nhập mật khẩu"
            return
        }
        val loginRequest = LoginRequest(email, password)
        loginViewModel.getLoginResponseLiveData(loginRequest)
            .observe(requireActivity(), { state -> processLoginResponse(state) })
    }

    private fun goToPasswordAssistant() {
        navController.navigate(R.id.action_loginFragment_to_passwordAssistantFragment)
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction("Thử lại 👍") { dismiss() } }
            .show()
    }

    private fun processLoginResponse(state: ScreenState<LoginApiResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    val loginUtils = LoginUtils(requireContext())
                    loginUtils.saveUserInfo(state.data)
                    Snackbar.make(requireView(), "Đăng nhập thành công", Snackbar.LENGTH_SHORT)
                        .show()
                    navController.navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    displayErrorSnackbar(state.message)
                }
            }
        }
    }
}