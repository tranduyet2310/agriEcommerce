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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CooperationAddressAdapter
import com.example.argiecommerce.databinding.FragmentCooperationAddressBinding
import com.example.argiecommerce.model.CooperationResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.utils.Constants.COOPERATION_KEY
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.CooperationViewModel
import com.example.argiecommerce.viewmodel.UserAddressViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class CooperationAddressFragment : Fragment() {
    private lateinit var binding: FragmentCooperationAddressBinding
    private lateinit var navController: NavController

    private lateinit var userAddressAdapter: CooperationAddressAdapter
    private val userAddrewssList: ArrayList<UserAddress> = arrayListOf()

    private val userAddressViewModel: UserAddressViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserAddressViewModel::class.java)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val cooperationViewModel: CooperationViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CooperationViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }

    private var user: User? = null
    private lateinit var alertDialog: AlertDialog
    private var selectedAddress: UserAddress? = null
    val args: CooperationAddressFragmentArgs by navArgs()
    private lateinit var cooperationResponse: CooperationResponse
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCooperationAddressBinding.inflate(inflater)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.address)

        user = userViewModel.user
        cooperationResponse = args.cooperation
        setupRecyclerView()
        getUserAddressData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }

        userAddressAdapter.onClick = {
            selectedAddress = it
        }

        binding.btnSave.setOnClickListener {
            if (selectedAddress == null) {
                Snackbar.make(requireView(), "Hãy chọn địa chỉ nhận hàng", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            updateCooperationAddress()
        }
    }

    private fun updateCooperationAddress() {
        val token = loginUtils.getUserToken()
        cooperationViewModel.updateCooperationAddress(
            token,
            cooperationResponse.id,
            selectedAddress!!.id
        ).observe(requireActivity(), { state -> processCooperationAddress(state) })
    }

    private fun getUserAddressData() {
        val token = loginUtils.getUserToken()
        userAddressViewModel.getAddressByUserId(token, user!!.id)
            .observe(requireActivity(), { state -> processUserAddress(state) })
    }

    private fun processCooperationAddress(state: ScreenState<CooperationResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    Snackbar.make(requireView(), "Cập nhật thành công", Snackbar.LENGTH_SHORT)
                        .show()
                    val b = Bundle().apply {
                        putParcelable(COOPERATION_KEY, cooperationResponse)
                    }
                    navController.navigate(R.id.action_cooperationAddressFragment_to_cooperationDetailFragment, b)
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

    private fun processUserAddress(state: ScreenState<ArrayList<UserAddress>?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    userAddrewssList.clear()
                    userAddrewssList.addAll(state.data)
                    userAddressAdapter.notifyDataSetChanged()
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

    private fun setupRecyclerView() {
        userAddressAdapter = CooperationAddressAdapter(userAddrewssList)
        binding.rcvUserAddress.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = userAddressAdapter
        }
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction(getString(R.string.retry_v2)) { dismiss() } }
            .show()
    }
}