package com.example.argiecommerce.view.profile

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentProfileBinding
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.model.UserApiResponse
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.UserInfoViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private var user: User? = null

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val userInfoViewModel: UserInfoViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }
    private lateinit var alertDialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        user = userViewModel.user

        if (user != null) getUserInfo()
        if (user?.avatar != null) showUserAvatar(user!!.avatar)
        if (user?.fullName != null) binding.tvUserName.text = user!!.fullName

        return binding.root
    }

    private fun getUserInfo() {
        userInfoViewModel.getUserInfo(user!!.id).observe(
            requireActivity(), { state -> processUserInfo(state) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        if (user == null) {
            navController.navigate(R.id.action_profileFragment_to_loginFragment)
        }

        binding.linearLogOut.setOnClickListener(this)
        binding.switchNotification.setOnClickListener(this)
        binding.constraintProfile.setOnClickListener(this)
        binding.linearAllOrders.setOnClickListener(this)
        binding.linearLocation.setOnClickListener(this)
        binding.linearWishList.setOnClickListener(this)
        binding.linearBilling.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.linearLogOut -> logOut()
            R.id.switchNotification -> notificationFunc()
            R.id.constraintProfile -> changeInfoAccount()
            R.id.linearLocation -> changeLocation()
            R.id.linearAllOrders -> seeAllOrders()
            R.id.linearWishList -> seeWishList()
            R.id.linearBilling -> billingProduct()
        }
    }

    private fun billingProduct() {
        navController.navigate(R.id.action_profileFragment_to_billingFragment)
    }

    private fun seeWishList() {
        navController.navigate(R.id.action_profileFragment_to_wishlistFragment)
    }

    private fun seeAllOrders() {
        navController.navigate(R.id.action_profileFragment_to_orderFragment)
    }

    private fun changeLocation() {
        navController.navigate(R.id.action_profileFragment_to_userAddressFragment)
    }

    private fun changeInfoAccount() {
        navController.navigate(R.id.action_profileFragment_to_userAccountFragment)
    }

    private fun notificationFunc() {
        Toast.makeText(requireContext(), "Cliked", Toast.LENGTH_SHORT).show()
    }

    private fun logOut() {
        val loginUtils = LoginUtils(requireContext())
        loginUtils.clearAll()
        userViewModel.user = null
        navController.navigate(R.id.action_profileFragment_to_loginFragment)
    }

    private fun processUserInfo(state: ScreenState<UserApiResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    val userApiResponse = state.data
                    user!!.fullName = userApiResponse.fullName
                    user!!.avatar = userApiResponse.avatar
                    user!!.phone = userApiResponse.phone
                    loginUtils.saveUserInfo(user!!)
                    userViewModel.user = user
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

    private fun showUserAvatar(imageUrl: String) {
        GlideApp.with(requireContext())
            .load(imageUrl)
            .into(binding.imageUser)
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction(Constants.RETRY) { dismiss() } }
            .show()
    }
}