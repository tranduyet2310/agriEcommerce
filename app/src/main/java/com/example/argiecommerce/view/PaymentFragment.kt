package com.example.argiecommerce.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentPaymentBinding
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.model.OrderDetailResponse
import com.example.argiecommerce.model.OrderRequest
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.network.Api
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.CartViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val apiService: Api by lazy {
        RetrofitClient.getInstance().getApi()
    }

    private lateinit var alertDialog: AlertDialog
    private var user: User? = null
    private var userAddress: UserAddress? = null
    private var totalPrice: Long = 0L
    private lateinit var paymentMethod: String
    private lateinit var paymentStatus: String
    private lateinit var cartProductList: ArrayList<CartResponse>
    private var orderCreated: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.result)

        user = userViewModel.user
        userAddress = userViewModel.userAddress
        totalPrice = userViewModel.total
        paymentMethod = userViewModel.paymentMethod
        paymentStatus = userViewModel.paymentStatus
        cartProductList = userViewModel.cartProductList
        orderCreated = userViewModel.orderCreated

        if (user != null && userAddress != null && !orderCreated) {
            lifecycleScope.launch {
                try {
                    val orderId: Long = createOrder()
                    if (orderId == 0L){
                        displayErrorSnackbar("Failed to get orderID")
                        cancel()
                    }
                    createOrderDetail(orderId)
                    increaseSoldProduct()
                    makeEmptyCart()
                } catch (e: Exception) {
                    Log.d("TEST", "Get error when create order")
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.btnHome.setOnClickListener {
            navController.navigate(R.id.action_paymentFragment_to_homeFragment)
        }
        binding.tvSeeOrder.setOnClickListener {
            navController.navigate(R.id.action_paymentFragment_to_orderFragment)
        }
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    suspend fun createOrder(): Long = suspendCoroutine { continuation ->
        val orderRequest = OrderRequest().apply {
            paymentStatus = this@PaymentFragment.paymentStatus
            paymentMethod = this@PaymentFragment.paymentMethod
            total = totalPrice
            addressId = userAddress!!.id
        }
        val token = loginUtils.getUserToken()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.createOrder(token, user!!.id, orderRequest)
                if (response.isSuccessful) {
                    val order = response.body()
                    val orderId = order?.id ?: 0L
                    continuation.resume(orderId)
                } else {
                    continuation.resume(0L)
                }
            } catch (e: Exception) {
                continuation.resume(0L)
            }
        }
    }

    suspend fun createOrderDetail(orderId: Long) {
        val token = loginUtils.getUserToken()
        cartProductList.map { cartItem ->
            val quantity = cartItem.quantity
            val productId = cartItem.product.productId
            val orderDetailResponse = OrderDetailResponse(quantity, productId)

            lifecycleScope.async(Dispatchers.IO) {
                val response = apiService.createOrderDetail(token, orderId, orderDetailResponse)
                if (response.isSuccessful) {
                    Log.d("TEST", "CREATED OrderDetail")
                }
            }
        }.awaitAll()
    }

    suspend fun increaseSoldProduct() {
        val token = loginUtils.getUserToken()
        cartProductList.map { cartItem ->
            val productId = cartItem.product.productId
            val quantity = cartItem.quantity.toLong()
            lifecycleScope.async(Dispatchers.IO) {
                val response = apiService.increaseSoldProduct(token, productId, quantity)
                if (response.isSuccessful) {
                    Log.d("TEST", "INCREASED!")
                }
            }
        }.awaitAll()
    }
    private fun makeEmptyCart() {
        val token = loginUtils.getUserToken()
        cartViewModel.deleteAllItems(token, user!!.id).observe(
            requireActivity(), { state -> processDeleteAllItems(state) }
        )
    }

    private fun processDeleteAllItems(state: ScreenState<MessageResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    userViewModel.orderCreated = true
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