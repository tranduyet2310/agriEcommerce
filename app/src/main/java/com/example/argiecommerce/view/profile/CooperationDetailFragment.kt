package com.example.argiecommerce.view.profile

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentCooperationDetailBinding
import com.example.argiecommerce.model.CooperationResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.OrderStatus
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils.Companion.formatPrice
import com.example.argiecommerce.utils.Utils.Companion.formatYield
import com.example.argiecommerce.viewmodel.CooperationViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class CooperationDetailFragment : Fragment() {
    private lateinit var binding: FragmentCooperationDetailBinding
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val cooperationViewModel: CooperationViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CooperationViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }

    private var user: User? = null
    private lateinit var alertDialog: AlertDialog

    val args: CooperationDetailFragmentArgs by navArgs()
    private lateinit var cooperationResponse: CooperationResponse
    private var status: OrderStatus = OrderStatus.PROCESSING
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCooperationDetailBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.cooperation_detail)

        cooperationResponse = args.cooperation
        user = userViewModel.user
        showData()
        setupStepView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        binding.btnCancel.setOnClickListener {
            if (status == OrderStatus.PROCESSING){
                updateCooperationStatus(OrderStatus.CANCELLED)
            } else if (status == OrderStatus.DELIVERING){
                updateCooperationStatus(OrderStatus.COMPLETED)
            }
        }
    }

    private fun updateCooperationStatus(status: OrderStatus) {
        val token = loginUtils.getUserToken()
        cooperationResponse.cooperationStatus = status
        cooperationViewModel.updateCooperationStatus(token, cooperationResponse.id, cooperationResponse)
            .observe(requireActivity(), { state -> processCooperationResponse(state) })
    }

    private fun showData() {
        binding.apply {
            tvShopName.text = cooperationResponse.shopName
            tvSupplierName.text = cooperationResponse.supplierName
            tvSupplierContact.text = cooperationResponse.supplierPhone
            tvCropsName.text = cooperationResponse.cropsName

            tvFullName.text = cooperationResponse.fullName
            tvUserContact.text = cooperationResponse.contact
            tvInvestment.text = cooperationResponse.investment.toLong().formatPrice()
            tvYield.text = formatYield(cooperationResponse.requireYield)
            tvDescription.text = cooperationResponse.description
        }
        status = cooperationResponse.cooperationStatus
    }

    private fun setupStepView() {
        val statusList: ArrayList<String> = arrayListOf()
        statusList.add(getString(R.string.PROCESSING))
        statusList.add(getString(R.string.CONFIRMED))
        statusList.add(getString(R.string.DELIVERING))
        statusList.add(getString(R.string.COMPLETED))

        binding.stepView.setSteps(statusList)
        binding.stepView.go(status.value, false)
        if (status == OrderStatus.COMPLETED) {
            binding.stepView.done(true)
            binding.btnCancel.text = getString(R.string.received)
        } else if (status == OrderStatus.CANCELLED) {
            binding.btnCancel.text = getString(R.string.terminated)
        } else if (status == OrderStatus.DELIVERING) {
            binding.btnCancel.text = getString(R.string.receive_success)
        }

        if (status == OrderStatus.CONFIRMED || status == OrderStatus.COMPLETED) {
            binding.btnCancel.isEnabled = false
            binding.btnCancel.setBackgroundColor(Color.parseColor("#E9EAEC"))
        }
    }

    private fun processCooperationResponse(state: ScreenState<CooperationResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    showSnackbar(getString(R.string.updated_status))
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    showSnackbar(state.message)
                }
            }
        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }
}