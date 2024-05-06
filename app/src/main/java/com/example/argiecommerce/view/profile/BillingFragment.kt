package com.example.argiecommerce.view.profile

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.BillingAddressAdapter
import com.example.argiecommerce.adapter.BillingProductAdapter
import com.example.argiecommerce.databinding.FragmentBillingBinding
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.CurrencyRate
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.paypal.token.CheckoutApi
import com.example.argiecommerce.paypal.token.CreatedOrder
import com.example.argiecommerce.paypal.token.OrderRepository
import com.example.argiecommerce.paypal.token.request.AmountRequest
import com.example.argiecommerce.paypal.token.request.ApplicationContextRequest
import com.example.argiecommerce.paypal.token.request.OrderRequest
import com.example.argiecommerce.paypal.token.request.PurchaseUnitRequest
import com.example.argiecommerce.utils.Constants.CURRENCY_API_KEY
import com.example.argiecommerce.utils.Constants.CURRENCY_FORMAT
import com.example.argiecommerce.utils.Constants.CURRENCY_FROM
import com.example.argiecommerce.utils.Constants.CURRENCY_TO
import com.example.argiecommerce.utils.Constants.MAX_ADDRESS
import com.example.argiecommerce.utils.Constants.PAID
import com.example.argiecommerce.utils.Constants.PAYMENT_COD
import com.example.argiecommerce.utils.Constants.PAYMENT_PAYPAL
import com.example.argiecommerce.utils.Constants.SCREEN_KEY
import com.example.argiecommerce.utils.Constants.TOTAL_ADDRESS
import com.example.argiecommerce.utils.Constants.UNPAID
import com.example.argiecommerce.utils.Constants.USD_FORMAT
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils.Companion.formatPrice
import com.example.argiecommerce.viewmodel.CartViewModel
import com.example.argiecommerce.viewmodel.UserAddressViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.CaptureOrderResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.IOException


class BillingFragment : Fragment(), View.OnClickListener {
    companion object {
        const val TAG = "BillingFragment"
    }

    private var _binding: FragmentBillingBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var billingAddressAdapter: BillingAddressAdapter
    private lateinit var billingProductAdapter: BillingProductAdapter

    private val userAddressViewModel: UserAddressViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserAddressViewModel::class.java)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }

    private lateinit var alertDialog: AlertDialog
    private lateinit var addressDialog: AlertDialog
    private var user: User? = null
    private val billingAddressList: ArrayList<UserAddress> = arrayListOf()
    private val billingProductList: ArrayList<CartResponse> = arrayListOf()
    private var totalPrice: Long = 0L
    private var selectedAddress: UserAddress? = null

    @OptIn(ExperimentalSerializationApi::class)
    private val checkoutApi = CheckoutApi()
    private val orderRepository = OrderRepository(checkoutApi)
    private val checkoutSdk: PayPalCheckout
        get() = PayPalCheckout
    private val uiScope = MainScope()
    private lateinit var orderId: String
    private var enteredAmount = totalPrice.toString()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBillingBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.billing)

        user = userViewModel.user

        setupRecyclerView()
        getUserAddress()
        getProductInCart()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
        binding.buttonPlaceOrder.setOnClickListener(this)
        binding.imageAddAddress.setOnClickListener(this)
        billingAddressAdapter.onClick = {
            selectedAddress = it
            userViewModel.userAddress = it
            val b = Bundle()
            b.putString(SCREEN_KEY, TAG)
            navController.navigate(R.id.action_billingFragment_to_addressDialog, b)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonPlaceOrder -> goToPaymentFragment()
            R.id.imageAddAddress -> goToAddressFragment()
        }
    }

    private fun goToAddressFragment() {
        if (billingAddressList.size == TOTAL_ADDRESS) {
            displayErrorSnackbar(MAX_ADDRESS)
        } else {
            navController.navigate(R.id.action_billingFragment_to_addressFragment)
        }

    }

    private fun goToPaymentFragment() {
        if (selectedAddress == null) {
            displayErrorSnackbar(getString(R.string.choose_address))
            return
        }

        if (billingProductList.isEmpty()){
            displayErrorSnackbar(getString(R.string.choose_product))
            return
        }

        if (binding.radioCoD.isChecked || binding.radioPaypal.isChecked) {
            if (binding.radioCoD.isChecked) {
                userViewModel.total = totalPrice
                userViewModel.userAddress = selectedAddress
                userViewModel.paymentMethod = PAYMENT_COD
                userViewModel.paymentStatus = UNPAID
                userViewModel.cartProductList = billingProductList
                userViewModel.orderCreated = false
                navController.navigate(R.id.action_billingFragment_to_paymentFragment)
            } else if (binding.radioPaypal.isChecked) {
                lifecycleScope.launch {
                    try {
                        convertVNDtoUSD()
                        Log.d("TEST", "entered_amount "+enteredAmount)
                        checkoutPayPal()
                    } catch (e: Exception) {
                        Log.d("TEST", e.message.toString())
                    }
                }
            }
        } else {
            displayErrorSnackbar(getString(R.string.choose_payment))
            return
        }
    }

    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            billingProductAdapter = BillingProductAdapter(billingProductList)
            adapter = billingProductAdapter
        }
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            billingAddressAdapter = BillingAddressAdapter(billingAddressList)
            adapter = billingAddressAdapter
        }
    }

    private fun getUserAddress() {
        userAddressViewModel.getAddressByUserId(user!!.id)
            .observe(requireActivity(), { state -> processUserAddress(state) })
    }

    private fun getProductInCart() {
        cartViewModel.getAllCartItems(user!!.id).observe(
            requireActivity(), { state -> processCartResponse(state) }
        )
    }

    private fun processUserAddress(state: ScreenState<ArrayList<UserAddress>?>) {
        when (state) {
            is ScreenState.Loading -> {
                addressDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    addressDialog.dismiss()
                    billingAddressList.clear()
                    billingAddressList.addAll(state.data)
                    billingAddressAdapter.notifyDataSetChanged()
                }
            }

            is ScreenState.Error -> {
                addressDialog.dismiss()
                if (state.message != null) {
                    displayErrorSnackbar(state.message)
                }
            }
        }
    }

    private fun processCartResponse(state: ScreenState<ArrayList<CartResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    billingProductList.clear()
                    billingProductList.addAll(state.data)
                    billingProductAdapter.notifyDataSetChanged()
                    totalPrice = 0L
                    calculateTotalPrice(billingProductList)
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
            .apply { setAction(getString(R.string.retry_v2)) { dismiss() } }
            .show()
    }

    private fun calculateTotalPrice(cartResponse: ArrayList<CartResponse>) {
        for (item in cartResponse) {
            totalPrice += if (item.product.discountPrice > 0) {
                item.product.discountPrice * item.quantity
            } else {
                item.product.standardPrice * item.quantity
            }
        }
        binding.tvTotalPrice.text = totalPrice.formatPrice()
    }

    // paypal
    private fun startCheckout() {
        checkoutSdk.startCheckout(
            createOrder = CreateOrder { createOrderActions ->
                uiScope.launch {
                    val createdOrder = createOrder()
                    createdOrder?.let {
                        createOrderActions.set(createdOrder.id)
                        orderId = createdOrder.id
                    }
                }
            }
        )

        checkoutSdk.registerCallbacks(
            onApprove = OnApprove { approval ->
                approval.orderActions.capture { result ->
                    val message = when (result) {
                        is CaptureOrderResult.Success -> {
                            Log.i(TAG, "Success: $result")
                            "💰 Order Capture Succeeded 💰"
                        }

                        is CaptureOrderResult.Error -> {
                            Log.i(TAG, "Error: $result")
                            "🔥 Order Capture Failed 🔥"
                        }
                    }
                    showSnackbar(message)
                    if (result is CaptureOrderResult.Success) {
                        userViewModel.total = totalPrice
                        userViewModel.userAddress = selectedAddress
                        userViewModel.paymentMethod = PAYMENT_PAYPAL
                        userViewModel.paymentStatus = PAID
                        userViewModel.cartProductList = billingProductList
                        userViewModel.orderCreated = false
                        navController.navigate(R.id.action_billingFragment_to_paymentFragment)
                    }
                }
            },
            onCancel = OnCancel {
                Log.d(TAG, "OnCancel")
                showSnackbar("😭 Buyer Cancelled Checkout 😭")
            },
            onError = OnError { errorInfo ->
                Log.d(TAG, "ErrorInfo: $errorInfo")
                showSnackbar("🚨 An Error Occurred 🚨")
            }
        )
    }

    private suspend fun createOrder(): CreatedOrder? {
        val orderRequest = createOrderRequest()
        return try {
            orderRepository.create(orderRequest)
        } catch (ex: IOException) {
            Log.w(TAG, "Attempt to create order failed with the following message: ${ex.message}")
            null
        }
    }

    private fun createOrderRequest(): OrderRequest {
        return OrderRequest(
            intent = OrderIntent.CAPTURE.name,
            applicationContext = ApplicationContextRequest(
                userAction = UserAction.PAY_NOW.name
            ),
            purchaseUnits = listOf(
                PurchaseUnitRequest(
                    amount = AmountRequest(
                        value = enteredAmount,
                        currencyCode = CurrencyCode.USD.name
                    )
                )
            )
        ).also { Log.i(TAG, "OrderRequest: $it") }
    }

    override fun onStop() {
        super.onStop()
        uiScope.coroutineContext.cancelChildren()
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
    }

    fun roundDouble(value: Double): String {
        return String.format(USD_FORMAT, value)
    }

    suspend fun convertVNDtoUSD() {
        withContext(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().getApi().convertCurrency(
                CURRENCY_API_KEY, CURRENCY_FROM, CURRENCY_TO, totalPrice.toDouble(), CURRENCY_FORMAT
            )
            if (response.isSuccessful) {
                val rates: CurrencyRate = response.body()!!.rates
                enteredAmount = roundDouble(rates.usd.result)
            } else {
                throw Exception("Failed to convert VND to USD")
            }
        }
    }

    suspend fun checkoutPayPal() {
        withContext(Dispatchers.Main) {
            startCheckout()
        }
    }
}