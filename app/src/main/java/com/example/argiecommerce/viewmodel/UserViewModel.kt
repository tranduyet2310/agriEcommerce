package com.example.argiecommerce.viewmodel

import androidx.lifecycle.ViewModel
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserAddress
import com.example.argiecommerce.utils.Constants.PAYMENT_COD
import com.example.argiecommerce.utils.Constants.UNPAID

class UserViewModel: ViewModel() {
    var user: User? = null
    var userAddress: UserAddress? = null
    var total: Long = 0L
    var paymentMethod: String = PAYMENT_COD
    var paymentStatus: String = UNPAID
    var cartProductList: ArrayList<CartResponse> = arrayListOf()
    var orderCreated: Boolean = false
    var supplierBasicInfo: SupplierBasicInfo? = null
    var category: CategoryApiResponse? = null
    // ID for standard
    var ocopId: Long = 0L
    var organicId: Long = 0L
    var globalGapId: Long = 0L
    var vietGapId: Long = 0L
    // ID for specialty
    var btbId: Long = 0L
    var dbsclId: Long = 0L
    var dbshId: Long = 0L
    var dhNtbId: Long = 0L
    var dbbId: Long = 0L
    var dnbId: Long = 0L
    var tbbId: Long = 0L
    var tnId: Long = 0L
}