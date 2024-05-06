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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.UserAddressAdapter
import com.example.argiecommerce.databinding.FragmentUserAddressBinding
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.Constants.SCREEN_KEY
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.UserAddressViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class UserAddressFragment : Fragment() {
    companion object {
        const val TAG = "UserAddressFragment"
    }
    private var _binding: FragmentUserAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var userAddressAdapter: UserAddressAdapter
    private val userAddrewssList: ArrayList<UserAddress> = arrayListOf()

    private val userAddressViewModel: UserAddressViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserAddressViewModel::class.java)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    private var user: User? = null
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAddressBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.address)

        user = userViewModel.user
        setupRecyclerView()
        getUserAddressData()

        return binding.root
    }

    private fun getUserAddressData() {
        userAddressViewModel.getAddressByUserId(user!!.id)
            .observe(requireActivity(), { state -> processUserAddress(state) })
    }

    private fun processUserAddress(state: ScreenState<ArrayList<UserAddress>?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
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
        userAddressAdapter = UserAddressAdapter(userAddrewssList)
        binding.rcvUserAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = userAddressAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.imageAddAddress.setOnClickListener {
            navController.navigate(R.id.action_userAddressFragment_to_addressFragment)
        }
        userAddressAdapter.onClick = {
            userViewModel.userAddress = it
            val b = Bundle()
            b.putString(SCREEN_KEY, TAG)
            navController.navigate(R.id.action_userAddressFragment_to_addressDialog, b)
        }
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction(getString(R.string.retry_v2)) { dismiss() } }
            .show()
    }
}