package com.example.argiecommerce.paypal

import android.app.Application
import com.example.argiecommerce.utils.Constants.PAYPAL_CLIENT_ID
import com.example.argiecommerce.utils.Constants.RETURN_URL
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

class QuickStartApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PayPalCheckout.setConfig(
            checkoutConfig = CheckoutConfig(
                application = this,
                clientId = PAYPAL_CLIENT_ID,
                environment = Environment.SANDBOX,
                currencyCode = CurrencyCode.USD,
                userAction = UserAction.PAY_NOW,
                settingsConfig = SettingsConfig(
                    loggingEnabled = true,
                    showWebCheckout = false
                ),
                returnUrl = RETURN_URL
            )
        )
    }
}