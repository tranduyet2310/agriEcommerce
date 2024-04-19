package com.example.argiecommerce.view.profile

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentCooperationDetailBinding
import com.example.argiecommerce.model.CooperationResponse
import com.example.argiecommerce.model.CurrencyRate
import com.example.argiecommerce.model.User
import com.example.argiecommerce.network.Api
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.paypal.token.CheckoutApi
import com.example.argiecommerce.paypal.token.CreatedOrder
import com.example.argiecommerce.paypal.token.OrderRepository
import com.example.argiecommerce.paypal.token.request.AmountRequest
import com.example.argiecommerce.paypal.token.request.ApplicationContextRequest
import com.example.argiecommerce.paypal.token.request.OrderRequest
import com.example.argiecommerce.paypal.token.request.PurchaseUnitRequest
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.Constants.COOPERATION_KEY
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.OrderStatus
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils.Companion.formatPrice
import com.example.argiecommerce.utils.Utils.Companion.formatYield
import com.example.argiecommerce.viewmodel.CooperationViewModel
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
    private val apiService: Api by lazy {
        RetrofitClient.getInstance().getApi()
    }

    private var user: User? = null
    private lateinit var alertDialog: AlertDialog

    val args: CooperationDetailFragmentArgs by navArgs()
    private lateinit var cooperationResponse: CooperationResponse
    private var status: OrderStatus = OrderStatus.PROCESSING
    private lateinit var currentAddress: String
    private lateinit var paymentAmount: String
    private var userAddressId: Long = 0L
    private var totalPrice: Long = 0L

    // payment variable
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
        binding = FragmentCooperationDetailBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.cooperation_detail)

        cooperationResponse = args.cooperation
        user = userViewModel.user
        showData()
        setupStepView()
        getDetailInfo()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        binding.btnCancel.setOnClickListener {
            when (status) {
                OrderStatus.PROCESSING -> {
                    updateCooperationStatus(OrderStatus.CANCELLED)
                    lifecycleScope.launch {
                        resetFieldYield()
                    }
                }
                OrderStatus.CONFIRMED -> {
                    if (totalPrice == 0L) {
                        showSnackbar("Bạn đã thanh toán trước đó")
                    } else if (binding.tvPayment.text.equals(getString(R.string.payment_50))) {
                        paymentAmount = getString(R.string.payment_100)
                        Log.d("TEST", "s " + paymentAmount)
                        paymentWithPaypal()
                    } else {
                        totalPrice = totalPrice / 2
                        paymentAmount = getString(R.string.payment_50)
                        Log.d("TEST", "s " + paymentAmount)
                        paymentWithPaypal()
                    }
                }
                OrderStatus.DELIVERING -> updateCooperationStatus(OrderStatus.COMPLETED)
                OrderStatus.COMPLETED -> {
                    if (binding.tvPayment.text.equals(getString(R.string.payment_50))) {
                        paymentAmount = getString(R.string.payment_100)
                        Log.d("TEST", "s " + paymentAmount)
                        paymentWithPaypal()
                    }
                }

                OrderStatus.CANCELLED -> {

                }
            }
        }

        binding.btnPayment.setOnClickListener {
            paymentAmount = getString(R.string.payment_100)
            Log.d("TEST", "s " + paymentAmount)
            paymentWithPaypal()
        }

        binding.imgEditAddress.setOnClickListener {
            val b = Bundle().apply {
                putParcelable(COOPERATION_KEY, cooperationResponse)
            }
            navController.navigate(
                R.id.action_cooperationDetailFragment_to_cooperationAddressFragment, b
            )
        }
    }

    suspend fun resetFieldYield(){
        val token = loginUtils.getUserToken()
        cooperationResponse.requireYield = 0.0
        withContext(Dispatchers.IO){
            val response = apiService.updateCooperationYield(token, cooperationResponse.id, cooperationResponse)
            if (response.isSuccessful){
                if (response.body() != null){
                    Log.d("TEST", "reset field yield successfully")
                } else {
                    Log.d("TEST", "message: "+response.message())
                }
            } else {
                Log.d("TEST", "message: "+response.message())
            }
        }
    }
    private fun paymentWithPaypal() {
        lifecycleScope.launch {
            try {
                convertVNDtoUSD()
                Log.d("TEST", "entered_amount " + enteredAmount)
                checkoutPayPal()
            } catch (e: Exception) {
                Log.d("TEST", e.message.toString())
            }
        }
    }

    private fun getDetailInfo() {
        cooperationViewModel.getCooperationById(cooperationResponse.id).observe(
            requireActivity(), { state -> processGetCooperation(state) }
        )
    }

    private fun updateCooperationStatus(status: OrderStatus) {
        val token = loginUtils.getUserToken()
        cooperationResponse.cooperationStatus = status
        cooperationViewModel.updateCooperationStatus(
            token,
            cooperationResponse.id,
            cooperationResponse
        )
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
        } else if (status == OrderStatus.CONFIRMED) {
            binding.btnCancel.text = getString(R.string.payment_50)
            binding.btnPayment.text = getString(R.string.payment_100)
            binding.btnPayment.visibility = View.VISIBLE
        }

//        if (status == OrderStatus.COMPLETED) {
//            binding.btnCancel.isEnabled = false
//            binding.btnCancel.setBackgroundColor(Color.parseColor("#E9EAEC"))
//        }
    }

    suspend fun getAddressById(addressId: Long) {
        withContext(Dispatchers.IO) {
            val response = apiService.getAddressByIdV2(addressId)
            if (response.isSuccessful) {
                val userAddress = response.body()
                if (userAddress != null) {
                    currentAddress =
                        "${userAddress.details} - ${userAddress.commune} - ${userAddress.district} - ${userAddress.province}"
                }
            }
        }
    }

    private fun processGetCooperation(state: ScreenState<CooperationResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    totalPrice = state.data.investment.toLong()
                    if (state.data.paymentStatus != null) {
                        binding.tvPayment.text = state.data.paymentStatus
                        if (state.data.paymentStatus.equals(getString(R.string.payment_50))) {
                            totalPrice = totalPrice / 2
                            binding.btnPayment.visibility = View.INVISIBLE
                            binding.btnCancel.text = getString(R.string.payment_remain)
                        } else if (state.data.paymentStatus.equals(getString(R.string.payment_100))) {
                            totalPrice = 0L
                            binding.btnPayment.visibility = View.INVISIBLE
                            binding.btnCancel.isEnabled = false
                            binding.btnCancel.text = getString(R.string.pay_all)
                        }
                        if (status == OrderStatus.DELIVERING) {
                            binding.btnCancel.text = getString(R.string.receive_success)
                            binding.btnCancel.isEnabled = true
                        }
                    }
                    if (state.data.addressId != null) {
                        userAddressId = state.data.addressId
                        lifecycleScope.launch {
                            getAddressById(state.data.addressId)
                            withContext(Dispatchers.Main) {
                                binding.tvAddress.text = currentAddress
                            }
                        }
                    }
                    alertDialog.dismiss()
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

    suspend fun createOrderInfo(orderRequest: com.example.argiecommerce.model.OrderRequest) {
        val token = loginUtils.getUserToken()
        withContext(Dispatchers.IO) {
            val response = apiService.createOrder(token, user!!.id, orderRequest)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    withContext(Dispatchers.Main) {
                        showSnackbar("Tạo đơn hàng thành công")
                    }
                } else {
                    Log.d("TEST", response.message())
                }
            } else {
                Log.d("TEST", response.message())
            }
        }
    }

    suspend fun updateCooperation() {
        val token = loginUtils.getUserToken()
        val cooperationRequest = CooperationResponse().apply {
            paymentStatus = paymentAmount
        }
        withContext(Dispatchers.IO) {
            val response = apiService.updateCooperationPayment(
                token,
                cooperationResponse.id,
                cooperationRequest
            )
            if (response.isSuccessful) {
                if (response.body() != null) {
                    withContext(Dispatchers.Main) {
                        showSnackbar("Cập nhật yêu cầu thành công")
                    }
                } else {
                    Log.d("TEST", response.message())
                }
            } else {
                Log.d("TEST", response.message())
            }
        }
    }

    // Payment
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
                            Log.i(BillingFragment.TAG, "Success: $result")
                            "💰 Order Capture Succeeded 💰"
                        }

                        is CaptureOrderResult.Error -> {
                            Log.i(BillingFragment.TAG, "Error: $result")
                            "🔥 Order Capture Failed 🔥"
                        }
                    }
                    showSnackbar(message)
                    if (result is CaptureOrderResult.Success) {
                        val orderRequest = com.example.argiecommerce.model.OrderRequest().apply {
                            paymentStatus = Constants.PAID
                            paymentMethod = Constants.PAYMENT_PAYPAL
                            total = totalPrice
                            addressId = userAddressId
                        }
                        lifecycleScope.launch {
                            withContext(Dispatchers.Main) {
                                alertDialog = progressDialog.createAlertDialog(requireActivity())
                            }
                            createOrderInfo(orderRequest)
                            updateCooperation()
                            withContext(Dispatchers.Main) {
                                alertDialog.dismiss()
                            }
                        }
                    }
                }
            },
            onCancel = OnCancel {
                Log.d(BillingFragment.TAG, "OnCancel")
                showSnackbar("😭 Buyer Cancelled Checkout 😭")
            },
            onError = OnError { errorInfo ->
                Log.d(BillingFragment.TAG, "ErrorInfo: $errorInfo")
                showSnackbar("🚨 An Error Occurred 🚨")
            }
        )
    }

    private suspend fun createOrder(): CreatedOrder? {
        val orderRequest = createOrderRequest()
        return try {
            orderRepository.create(orderRequest)
        } catch (ex: IOException) {
            Log.w(
                BillingFragment.TAG,
                "Attempt to create order failed with the following message: ${ex.message}"
            )
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
        ).also { Log.i(BillingFragment.TAG, "OrderRequest: $it") }
    }

    fun roundDouble(value: Double): String {
        return String.format(Constants.USD_FORMAT, value)
    }

    suspend fun convertVNDtoUSD() {
        withContext(Dispatchers.Main) {
            alertDialog = progressDialog.createAlertDialog(requireActivity())
        }
        withContext(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().getApi().convertCurrency(
                Constants.CURRENCY_API_KEY,
                Constants.CURRENCY_FROM,
                Constants.CURRENCY_TO, totalPrice.toDouble(),
                Constants.CURRENCY_FORMAT
            )
            if (response.isSuccessful) {
                val rates: CurrencyRate = response.body()!!.rates
                enteredAmount = roundDouble(rates.usd.result)
            } else {
                throw Exception("Failed to convert VND to USD")
            }
        }
        withContext(Dispatchers.Main) {
            alertDialog.dismiss()
        }
    }

    suspend fun checkoutPayPal() {
        withContext(Dispatchers.Main) {
            startCheckout()
        }
    }

    override fun onStop() {
        super.onStop()
        uiScope.coroutineContext.cancelChildren()
    }
}