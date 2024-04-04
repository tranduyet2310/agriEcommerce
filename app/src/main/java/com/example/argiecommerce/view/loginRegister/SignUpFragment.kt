package com.example.argiecommerce.view.loginRegister

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentSignUpBinding
import com.example.argiecommerce.model.RegisterApiResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Validation
import com.example.argiecommerce.viewmodel.RegisterViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val registerViewModel: RegisterViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    private lateinit var alertDialog: AlertDialog
    private val user: User by lazy { User() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.tvLogin.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvLogin -> goToLoginFragment()
            R.id.btnSignUp -> createAccount()
        }
    }

    private fun createAccount() {
        val name = binding.edtUserName.text.toString()
        val phone = binding.edtUserPhone.text.toString()
        val email = binding.edtUserEmail.text.toString()
        val password = binding.edtUserPassword.text.toString()

        user.email = email
        user.fullName = name
        user.phone = phone
        user.password = password

        if (name.isEmpty()) {
            binding.edtUserName.setError("Yêu cầu nhập tên")
            binding.edtUserName.requestFocus()
            return
        }

        if (!Validation.isValidName(name)) {
            binding.edtUserName.setError("Tên phải tối thiểu 3 ký tự")
            binding.edtUserName.requestFocus()
            return
        }

        if (phone.isEmpty()) {
            binding.edtUserPhone.setError("Yêu cầu nhập số điện thoại")
            binding.edtUserPhone.requestFocus()
            return
        }

        if (!Validation.isValidPhone(phone)) {
            binding.edtUserPhone.setError("Số điện thoại không hợp lệ")
            binding.edtUserPhone.requestFocus()
            return
        }

        if (email.isEmpty()) {
            binding.edtUserEmail.setError("Yêu cầu nhập email")
            binding.edtUserEmail.requestFocus()
            return
        }

        if (!Validation.isValidEmail(email)) {
            binding.edtUserEmail.setError("Email không đúng định dạng")
            binding.edtUserEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.edtUserPassword.setError("Yêu cầu nhập mật khẩu")
            binding.edtUserPassword.requestFocus()
            return
        }

        if (!Validation.isValidPassword(password)) {
            binding.edtUserPassword.setError("Mật khẩu phải tối thiểu 8 ký tự")
            binding.edtUserPassword.requestFocus()
            return
        }

        registerViewModel.getRegisterResponseLiveData(user).observe(requireActivity(), { state ->
            processRegisterResponse(state)
        })
        Snackbar.make(requireView(), "Tạo tài khoản thành công", Snackbar.LENGTH_SHORT).show()
    }

    private fun goToLoginFragment() {
        navController.navigate(R.id.action_signUpFragment_to_loginFragment)
    }

    private fun processRegisterResponse(state: ScreenState<RegisterApiResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    val loginUtils = LoginUtils(requireContext())
                    user.id = state.data.id
                    loginUtils.saveUserInfo(user)
                    Snackbar.make(requireView(), "Đăng ký thành công", Snackbar.LENGTH_SHORT)
                        .show()
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

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction("Thử lại 👍") { dismiss() } }
            .show()
    }

}