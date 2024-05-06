package com.example.argiecommerce.view.profile

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
import com.example.argiecommerce.databinding.FragmentAddressBinding
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.UserAddressViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class AddressFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val userAddressViewModel: UserAddressViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserAddressViewModel::class.java)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }

    private var user: User? = null
    private lateinit var alertDialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        user = userViewModel.user
        binding.toolbarLayout.titleToolbar.text = getString(R.string.add_address)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.buttonDelelte.setOnClickListener(this)
        binding.buttonSave.setOnClickListener(this)
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonSave -> saveToDatabase()
            R.id.buttonDelelte -> clearText()
        }
    }

    private fun clearText() {
        binding.edtFullName.text.clear()
        binding.edtPhone.text.clear()
        binding.edtCity.text.clear()
        binding.edtState.text.clear()
        binding.edtStreet.text.clear()
        binding.edtAddressDetail.text.clear()
    }

    private fun saveToDatabase() {
        val contactName = binding.edtFullName.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()
        val province = binding.edtCity.text.toString().trim()
        val district = binding.edtState.text.toString().trim()
        val commune = binding.edtStreet.text.toString().trim()
        val details = binding.edtAddressDetail.text.toString().trim()

        if (contactName.isEmpty() || phone.isEmpty() || province.isEmpty() ||
            district.isEmpty() || contactName.isEmpty() || details.isEmpty()
        ) {
            displayErrorSnackbar(getString(R.string.field_required))
        } else {
            val userAddress = UserAddress(contactName, phone, province, district, commune, details)
            val token = loginUtils.getUserToken()
            userAddressViewModel.createNewAddress(token, user!!.id, userAddress)
                .observe(requireActivity(), { state -> processUserAddress(state) })
        }
    }

    private fun processUserAddress(state: ScreenState<UserAddress?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    Snackbar.make(
                        requireView(),
                        getString(R.string.create_new_address_successfully),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    goToAddressFragment()
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

    private fun goToAddressFragment() {
        navController.navigateUp()
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction(getString(R.string.retry_v2)) { dismiss() } }
            .show()
    }
}