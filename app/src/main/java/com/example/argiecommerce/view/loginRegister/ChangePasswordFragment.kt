package com.example.argiecommerce.view.loginRegister

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentChangePasswordBinding
import com.example.argiecommerce.model.PasswordRequest
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserApiResponse
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.Constants.MIN_PASSWORD_LENGTH
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.UserInfoViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class ChangePasswordFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val userInfoViewModel: UserInfoViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }

    private var user: User? = null
    private lateinit var alertDialog: AlertDialog
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }

    private lateinit var passwordRequest: PasswordRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        user = userViewModel.user

        if (user == null) {
            binding.cancel.visibility = View.INVISIBLE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.cancel.setOnClickListener(this)
        binding.saveChanges.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cancel -> goToProfileFragment()
            R.id.saveChanges -> changeUserPassword()
        }
    }

    private fun changeUserPassword() {
        val currentPass = binding.edtCurrentPassword.text.toString().trim()
        val newPass = binding.edtNewPassword.text.toString().trim()
        val retypePass = binding.edtRetypePassword.text.toString().trim()

        if (currentPass.isEmpty() || newPass.isEmpty() || retypePass.isEmpty()) {
            displayErrorSnackbar(Constants.FIELD_REQUIRED)
        } else if (!user?.password.equals(currentPass)) {
            displayErrorSnackbar(getString(R.string.current_pass_not_correct))
        } else if (newPass.length < MIN_PASSWORD_LENGTH) {
            displayErrorSnackbar(getString(R.string.using_strong_password))
        } else if (!newPass.equals(retypePass)) {
            displayErrorSnackbar(getString(R.string.retype_pass_not_correct))
        } else {
            passwordRequest = PasswordRequest(currentPass, newPass)
            val token = loginUtils.getUserToken()
            userInfoViewModel.changePassword(token, user!!.id, passwordRequest).observe(
                requireActivity(), { state -> processUserPassword(state) }
            )
        }
    }

    private fun goToProfileFragment() {
        navController.navigate(R.id.action_changePasswordFragment_to_userAccountFragment)
    }

    private fun processUserPassword(state: ScreenState<UserApiResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    user!!.password = passwordRequest.newPass
                    loginUtils.saveUserInfo(user!!)
                    userViewModel.user = user
                    Snackbar.make(
                        requireView(),
                        getString(R.string.update_info),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    navController.navigateUp()
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
            .apply { setAction(Constants.RETRY) { dismiss() } }
            .show()
    }
}