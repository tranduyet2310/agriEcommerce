package com.example.argiecommerce.view.profile

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentAddressBinding
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.UserAddressViewModel
import com.example.argiecommerce.viewmodel.UserViewModel

class AddressDialog : DialogFragment() {
    private lateinit var binding: FragmentAddressBinding

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
    private var userAddress: UserAddress? = null
    private lateinit var alertDialog: AlertDialog
    private lateinit var navController: NavController
    val args: AddressDialogArgs by navArgs()
    private lateinit var currentScreen: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        binding.toolbarLayout.titleToolbar.visibility = View.GONE

        user = userViewModel.user
        userAddress = userViewModel.userAddress
        currentScreen = args.screen
        showInfo()

        return binding.root
    }

    private fun showInfo() {
        binding.edtFullName.text =
            Editable.Factory.getInstance().newEditable(userAddress?.contactName)
        binding.edtPhone.text = Editable.Factory.getInstance().newEditable(userAddress?.phone)
        binding.edtCity.text = Editable.Factory.getInstance().newEditable(userAddress?.province)
        binding.edtState.text = Editable.Factory.getInstance().newEditable(userAddress?.district)
        binding.edtStreet.text = Editable.Factory.getInstance().newEditable(userAddress?.commune)
        binding.edtAddressDetail.text =
            Editable.Factory.getInstance().newEditable(userAddress?.details)

        binding.imgClose.visibility = View.VISIBLE
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
            binding.tvFailed.visibility = View.VISIBLE
            binding.tvFailed.text = Constants.FIELD_REQUIRED
        } else {
            val updateUserAddress =
                UserAddress(contactName, phone, province, district, commune, details)
            val token = loginUtils.getUserToken()
            userAddressViewModel.updateAddress(
                token,
                user!!.id,
                userAddress!!.id,
                updateUserAddress
            )
                .observe(requireActivity(), { state -> processUpdateUserAddress(state) })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.buttonDelelte.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireActivity())
            dialogBuilder.setTitle(getString(R.string.check_out))
            dialogBuilder.setMessage(getString(R.string.delete_address))
            dialogBuilder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
                dialog.dismiss()
                deleteAddress()
                if (currentScreen.equals(BillingFragment.TAG)){
                    navController.navigate(R.id.action_addressDialog_to_billingFragment)
                } else if (currentScreen.equals(UserAddressFragment.TAG)){
                    navController.navigate(R.id.action_addressDialog_to_userAddressFragment)
                }
            }
            dialogBuilder.setNegativeButton(getString(R.string.cancel_v2)) { dialog, which ->
                dialog.dismiss()
            }
            dialogBuilder.create().show()
        }
        binding.buttonSave.setOnClickListener {
            if (userAddress != null) {
                saveToDatabase()
                binding.tvFailed.visibility = View.GONE
                if (currentScreen.equals(BillingFragment.TAG)){
                    navController.navigate(R.id.action_addressDialog_to_billingFragment)
                } else if (currentScreen.equals(UserAddressFragment.TAG)){
                    navController.navigate(R.id.action_addressDialog_to_userAddressFragment)
                }
            } else {
                binding.tvFailed.visibility = View.VISIBLE
                binding.tvFailed.text = Constants.ADDRESS_ID_NOT_FOUND
            }
        }
        binding.imgClose.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun deleteAddress() {
        val token = loginUtils.getUserToken()
        userAddressViewModel.deleteAddress(token, user!!.id, userAddress!!.id)
            .observe(requireActivity(), { state -> processDeleteUserAddress(state) })
    }

    private fun processUpdateUserAddress(state: ScreenState<UserAddress?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    binding.tvFailed.visibility = View.GONE
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    binding.tvFailed.visibility = View.VISIBLE
                    binding.tvFailed.text = state.message
                }
            }
        }
    }

    private fun processDeleteUserAddress(state: ScreenState<MessageResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    binding.tvFailed.visibility = View.GONE
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    Log.d("TEST", state.message)
                    binding.tvFailed.visibility = View.VISIBLE
                    binding.tvFailed.text = state.message
                }
            }
        }
    }
}