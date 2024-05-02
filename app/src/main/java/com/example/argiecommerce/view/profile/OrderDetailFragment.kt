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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.BillingProductAdapter
import com.example.argiecommerce.databinding.FragmentOrderDetailBinding
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.OrderDetailResponse
import com.example.argiecommerce.model.OrderResponse
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.OrderStatus
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils.Companion.formatPrice
import com.example.argiecommerce.viewmodel.OrderViewModel
import com.example.argiecommerce.viewmodel.UserAddressViewModel
import com.google.android.material.snackbar.Snackbar


class OrderDetailFragment : Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider(requireActivity()).get(OrderViewModel::class.java)
    }
    private val userAddressViewModel: UserAddressViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserAddressViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }

    val args: OrderDetailFragmentArgs by navArgs()
    private lateinit var billingProductAdapter: BillingProductAdapter
    private val billingProductList: ArrayList<CartResponse> = arrayListOf()
    private var orderStatus: OrderStatus = OrderStatus.PROCESSING
    private lateinit var orderResponse: OrderResponse
    private lateinit var alertDialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.order_detail)

        setupRecyclerView()
        getData()
        setupStepView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        binding.btnCancelOrder.setOnClickListener {
            if (orderStatus == OrderStatus.PROCESSING){
                updateOrderStatus(OrderStatus.CANCELLED.name)
            } else if (orderStatus == OrderStatus.DELIVERING){
                updateOrderStatus(OrderStatus.COMPLETED.name)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        orderResponse = args.order
        binding.tvOrderId.text = orderResponse.orderNumber
        binding.tvPaymentMethod.text =
            if (orderResponse.paymentMethod.equals("CoD")) getString(R.string.cod)
            else getString(R.string.paypal)
        binding.tvTotalPrice.text = orderResponse.total.formatPrice()
        orderStatus = orderResponse.orderStatus
        val addressId = orderResponse.addressId

        val orderDetails: MutableList<OrderDetailResponse> = orderResponse.orderDetails
        for (orderDetail in orderDetails) {
            val cartResponse = CartResponse()
            cartResponse.quantity = orderDetail.quantity
            cartResponse.product = orderDetail.product
            billingProductList.add(cartResponse)
        }
        billingProductAdapter.notifyDataSetChanged()

        getUserAddress(addressId)
    }

    private fun getUserAddress(addressId: Long) {
        userAddressViewModel.getAddressById(addressId).observe(
            requireActivity(), { state -> processUserAddress(state) }
        )
    }

    private fun updateOrderStatus(status: String){
        val token = loginUtils.getUserToken()
        val orderId = orderResponse.id
        orderViewModel.updateOrderStatus(token, orderId, status).observe(
            requireActivity(), { state -> processOrderStatus(state) }
        )
    }
    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            billingProductAdapter = BillingProductAdapter(billingProductList)
            adapter = billingProductAdapter
        }
    }

    private fun setupStepView() {
        val statusList: ArrayList<String> = arrayListOf()
        statusList.add(getString(R.string.PROCESSING))
        statusList.add(getString(R.string.CONFIRMED))
        statusList.add(getString(R.string.DELIVERING))
        statusList.add(getString(R.string.COMPLETED))

        binding.stepView.setSteps(statusList)
        binding.stepView.go(orderStatus.value, false)
        if (orderStatus == OrderStatus.COMPLETED) {
            binding.stepView.done(true)
            binding.btnCancelOrder.text = getString(R.string.received)
        } else if (orderStatus == OrderStatus.CANCELLED) {
            binding.btnCancelOrder.text = getString(R.string.CANCELLED)
        } else if (orderStatus == OrderStatus.DELIVERING) {
            binding.btnCancelOrder.text = getString(R.string.receive_success)
        }

        if (orderStatus == OrderStatus.CONFIRMED || orderStatus == OrderStatus.COMPLETED) {
            binding.btnCancelOrder.isEnabled = false
            binding.btnCancelOrder.setBackgroundColor(Color.parseColor("#E9EAEC"))
        }
    }

    private fun processUserAddress(state: ScreenState<UserAddress?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    binding.tvFullName.text = state.data.contactName
                    binding.tvPhoneNumber.text = state.data.phone
                    val address = "${state.data.details} - ${state.data.commune} - ${state.data.district} - ${state.data.province}"
                    binding.tvAddress.text = address
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

    private fun processOrderStatus(state: ScreenState<OrderResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    Snackbar.make(requireView(), getString(R.string.updated_status), Snackbar.LENGTH_SHORT).show()
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