package com.example.argiecommerce.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentPaymentBinding
import com.example.argiecommerce.paypal.token.CheckoutApi
import com.example.argiecommerce.paypal.token.CreatedOrder
import com.example.argiecommerce.paypal.token.OrderRepository
import com.example.argiecommerce.paypal.token.request.AmountRequest
import com.example.argiecommerce.paypal.token.request.ApplicationContextRequest
import com.example.argiecommerce.paypal.token.request.OrderRequest
import com.example.argiecommerce.paypal.token.request.PurchaseUnitRequest
import com.google.android.material.snackbar.Snackbar
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.AuthorizeOrderResult
import com.paypal.checkout.order.CaptureOrderResult
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.io.IOException


class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding
    private lateinit var navController: NavController

    private lateinit var orderId: String
    private val tag = javaClass.simpleName

    private val checkoutApi = CheckoutApi()
    private val orderRepository = OrderRepository(checkoutApi)

    private val checkoutSdk: PayPalCheckout
        get() = PayPalCheckout

    private val selectedUserAction: UserAction
        get() {
            return when (val selectedId = binding.selectUserAction.checkedRadioButtonId) {
                R.id.userActionOptionContinue -> UserAction.CONTINUE
                R.id.userActionOptionPayNow -> UserAction.PAY_NOW
                else -> {
                    throw IllegalArgumentException(
                        "Expected one of the following ids: ${R.id.userActionOptionContinue}, or " +
                                "${R.id.userActionOptionPayNow} but was $selectedId"
                    )
                }
            }
        }

    private val selectedOrderIntent: OrderIntent
        get() {
            return when (val selectedId = binding.selectOrderIntent.checkedRadioButtonId) {
                R.id.orderIntentOptionAuthorize -> OrderIntent.AUTHORIZE
                R.id.orderIntentOptionCapture -> OrderIntent.CAPTURE
                else -> {
                    throw IllegalArgumentException(
                        "Expected one of the following ids: ${R.id.orderIntentOptionAuthorize}, or " +
                                "${R.id.orderIntentOptionCapture} but was $selectedId"
                    )
                }
            }
        }

    private val selectedCurrencyCode: CurrencyCode
        get() {
            return when (val selectedId = binding.selectCurrencyCode.checkedRadioButtonId) {
                R.id.currencyCodeUsd -> CurrencyCode.USD
                R.id.currencyCodeEur -> CurrencyCode.EUR
                R.id.currencyCodeGbp -> CurrencyCode.GBP
                else -> {
                    throw IllegalArgumentException(
                        "Expected one of the following ids: ${R.id.currencyCodeUsd}, " +
                                "${R.id.currencyCodeEur}, or ${R.id.currencyCodeGbp} but was $selectedId"
                    )
                }
            }
        }

    private val enteredAmount: String
        get() = binding.totalAmountInput.editText!!.text.toString()

    private val uiScope = MainScope()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater)

//        paymentOrder()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.totalAmountInput.editText?.addTextChangedListener {
            binding.totalAmountInput.error = null
        }

        binding.submitTokenButton.setOnClickListener {
            if (binding.totalAmountInput.editText!!.text.isEmpty()) {
                binding.totalAmountInput.error = "Total Amount Required"
                return@setOnClickListener
            }

            startCheckout()
        }

    }

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
                Log.i(tag, "OnApprove: $approval")
                when (selectedOrderIntent) {
                    OrderIntent.AUTHORIZE -> approval.orderActions.authorize { result ->
                        val message = when (result) {
                            is AuthorizeOrderResult.Success -> {
                                Log.i(tag, "Success: $result")
                                "💰 Order Authorization Succeeded 💰"
                            }
                            is AuthorizeOrderResult.Error -> {
                                Log.i(tag, "Error: $result")
                                "🔥 Order Authorization Failed 🔥"
                            }
                        }
                        showSnackbar(message)
                    }
                    OrderIntent.CAPTURE -> approval.orderActions.capture { result ->
                        val message = when (result) {
                            is CaptureOrderResult.Success -> {
                                Log.i(tag, "Success: $result")
                                "💰 Order Capture Succeeded 💰"
                            }
                            is CaptureOrderResult.Error -> {
                                Log.i(tag, "Error: $result")
                                "🔥 Order Capture Failed 🔥"
                            }
                        }
                        showSnackbar(message)
                    }
                }
            },
            onCancel = OnCancel {
                Log.d(tag, "OnCancel")
                showSnackbar("😭 Buyer Cancelled Checkout 😭")
            },
            onError = OnError { errorInfo ->
                Log.d(tag, "ErrorInfo: $errorInfo")
                showSnackbar("🚨 An Error Occurred 🚨")
            }
        )
    }

    private suspend fun createOrder(): CreatedOrder? {
        val orderRequest = createOrderRequest()
        return try {
            orderRepository.create(orderRequest)
        } catch (ex: IOException) {
            Log.w(tag, "Attempt to create order failed with the following message: ${ex.message}")
            null
        }
    }

    private fun createOrderRequest(): OrderRequest {
        return OrderRequest(
            intent = selectedOrderIntent.name,
            applicationContext = ApplicationContextRequest(
                userAction = selectedUserAction.name
            ),
            purchaseUnits = listOf(
                PurchaseUnitRequest(
                    amount = AmountRequest(
                        value = enteredAmount,
                        currencyCode = selectedCurrencyCode.name
                    )
                )
            )
        ).also { Log.i(tag, "OrderRequest: $it") }
    }

    override fun onStop() {
        super.onStop()
        uiScope.coroutineContext.cancelChildren()
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
    }
}