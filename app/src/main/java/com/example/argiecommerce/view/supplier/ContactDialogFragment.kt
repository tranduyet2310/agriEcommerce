package com.example.argiecommerce.view.supplier

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentContactDialogBinding
import com.example.argiecommerce.model.CooperationResponse
import com.example.argiecommerce.model.FieldApiResponse
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.model.User
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
//import com.example.argiecommerce.utils.Constants.FIELD_REQUIRED
import com.example.argiecommerce.utils.Constants.KG_UNIT
import com.example.argiecommerce.utils.Constants.TAN_UNIT
import com.example.argiecommerce.utils.Constants.TA_UNIT
import com.example.argiecommerce.utils.Constants.YEN_UNIT
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.OrderStatus
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils
import com.example.argiecommerce.utils.Utils.Companion.convertToLong
import com.example.argiecommerce.utils.Utils.Companion.formatPrice
import com.example.argiecommerce.view.MainActivity
import com.example.argiecommerce.viewmodel.CooperationViewModel
import com.example.argiecommerce.viewmodel.SupplierViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ContactDialogFragment : AppCompatDialogFragment() {
    private lateinit var binding: FragmentContactDialogBinding

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val supplierViewModel: SupplierViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SupplierViewModel::class.java)
    }
    private val cooperationViewModel: CooperationViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CooperationViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }

    private var user: User? = null
    private var supplierBasicInfo: SupplierBasicInfo? = null
    private lateinit var alertDialog: AlertDialog

    private val massUnit: ArrayList<String> = arrayListOf()
    private val cropsList: ArrayList<String> = arrayListOf()
    private var coefficient: Int = 1
    private val cropsHashMap: HashMap<String, Long> = hashMapOf()
    private val yieldHashMap: HashMap<String, Double> = hashMapOf()
    private val priceHashMap: HashMap<String, Long> = hashMapOf()
    private lateinit var cropsName: String
    private var cropsCurrentTotal: Double = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactDialogBinding.inflate(inflater, container, false)

        user = userViewModel.user
        supplierBasicInfo = userViewModel.supplierBasicInfo

        setupSpinner()
        if (supplierBasicInfo != null) getCropsName()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinnerMassListener()
        setupSpinnerCropsListener()

        binding.edtYieldsDialog.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtYieldsDialog.text.toString().isEmpty()) {
                    binding.edtInvestDialog.text = Editable.Factory.getInstance().newEditable("0")
                } else {
                    autoCalculatePrice()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        binding.btnSave.setOnClickListener {
            if (!binding.checkboxAgree.isChecked) {
                showSnackbar(getString(R.string.must_agree))
            } else if (!validateInfoField()) {
                showSnackbar(getString(R.string.field_required))
            } else if (!validateYieldThreshold()) {
                showSnackbar(getString(R.string.exceed_threshold))
            } else if (!validatePriceThreshold()) {
                showSnackbar(getString(R.string.lower_than_threshold))
            } else {
                if (supplierBasicInfo != null) {
                    val token = loginUtils.getUserToken()
                    val cooperationResponse = createRequest()
                    cooperationViewModel.createCooperation(
                        token,
                        supplierBasicInfo!!.supplierId,
                        cooperationResponse
                    ).observe(
                        requireActivity(), { state -> processCooperationResponse(state) }
                    )
                } else {
                    dialog?.dismiss()
                }
            }
        }

        binding.policy.setOnClickListener {
            val action =
                ContactDialogFragmentDirections.actionContactDialogFragmentToPolicyFragment()
            this@ContactDialogFragment.findNavController().navigate(action)
        }
    }

    private fun setupSpinnerCropsListener() {
        binding.spCropsDialog.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cropsName = cropsList[position]
                val fieldId = cropsHashMap.get(cropsName)
                lifecycleScope.launch(Dispatchers.IO) {
                    if (fieldId != null) {
                        val response = RetrofitClient.getInstance().getApi()
                            .calculateCurrentTotal(fieldId, supplierBasicInfo!!.supplierId)
                        cropsCurrentTotal = response.body()?.message?.toDouble()!!
                    }
                }
                binding.tvPricePerKg.visibility = View.VISIBLE
                val cropsPricePerKg =
                    "Giá thành: ${(priceHashMap.get(cropsName)?.formatPrice() ?: "0")} đ/1 kg"
                binding.tvPricePerKg.text = cropsPricePerKg

                binding.edtYieldsDialog.text.clear()
                binding.edtInvestDialog.text.clear()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("TEST", "in ContactDialogFragment - nothing to show")
            }
        }
    }

    private fun setupSpinnerMassListener() {
        binding.spMassUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = massUnit[position]
                when (value) {
                    KG_UNIT -> coefficient = 1
                    YEN_UNIT -> coefficient = 10
                    TA_UNIT -> coefficient = 100
                    TAN_UNIT -> coefficient = 1000
                }

                if (!binding.edtYieldsDialog.text.toString().isEmpty()){
                    autoCalculatePrice()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("TEST", "in ContactDialogFragment - nothing to show")
            }
        }
    }

    private fun autoCalculatePrice() {
        val yield = binding.edtYieldsDialog.text.toString().trim().toDouble()
        val requiredYield = yield * coefficient
        val thresholdPerKg = priceHashMap.get(cropsName)
        val thresholdAccept = thresholdPerKg?.times(requiredYield)
        val price = thresholdAccept?.toLong()?.formatPrice() ?: "0"
        binding.edtInvestDialog.text = Editable.Factory.getInstance().newEditable(price)
    }

    private fun validatePriceThreshold(): Boolean {
        val investment = binding.edtInvestDialog.text.toString().trim()
        val investmentValue = convertToLong(investment).toDouble()
        val yield = binding.edtYieldsDialog.text.toString().trim().toDouble()
        val requiredYield = yield * coefficient

        val thresholdPerKg = priceHashMap.get(cropsName)
        val thresholdAccept = thresholdPerKg?.times(requiredYield)
        if (investmentValue == 0.0){
            return false
        } else if (investmentValue < thresholdAccept!!) {
            return false
        }
        return true
    }

    private fun validateYieldThreshold(): Boolean {
        val yield = binding.edtYieldsDialog.text.toString().trim()
        val yieldNumber = yield.toDouble()
        val requiredYield = yieldNumber * coefficient

        val thresholdTotal = yieldHashMap.get(cropsName)
        val thresholdAccept = thresholdTotal?.minus(cropsCurrentTotal)
        if (requiredYield > thresholdAccept!!) {
            return false
        }
        return true
    }

    private fun validateInfoField(): Boolean {
        val fullName = binding.edtNameDialog.text.toString().trim()
        val description = binding.edtDescriptionDialog.text.toString().trim()
        val investment = binding.edtInvestDialog.text.toString().trim()
        val contact = binding.edtContactDialog.text.toString().trim()
        val yield = binding.edtYieldsDialog.text.toString().trim()

        if (fullName.isEmpty() || description.isEmpty() || investment.isEmpty()
            || contact.isEmpty() || yield.isEmpty()
        ) {
            return false
        }
        return true
    }

    private fun createRequest(): CooperationResponse {
        val fullName = binding.edtNameDialog.text.toString().trim()
        val description = binding.edtDescriptionDialog.text.toString().trim()
        val investment = binding.edtInvestDialog.text.toString().trim()
        val contact = binding.edtContactDialog.text.toString().trim()
        val yield = binding.edtYieldsDialog.text.toString().trim()

        val yieldNumber = yield.toDouble()
        val requiredYield = yieldNumber * coefficient

        val fieldId = cropsHashMap.get(cropsName)

        val cooperationResponse = CooperationResponse()
        cooperationResponse.fullName = fullName
        cooperationResponse.description = description
        cooperationResponse.investment = convertToLong(investment).toString()
        cooperationResponse.contact = contact
        cooperationResponse.requireYield = requiredYield
        cooperationResponse.supplierId = supplierBasicInfo!!.supplierId
        cooperationResponse.userId = user!!.id
        cooperationResponse.cropsName = cropsName
        if (fieldId != null) {
            cooperationResponse.fieldId = fieldId
        }
        cooperationResponse.cooperationStatus = OrderStatus.PROCESSING

        return cooperationResponse
    }

    private fun getCropsName() {
        supplierViewModel.getFieldInfo(supplierBasicInfo!!.supplierId).observe(
            requireActivity(), { state -> processFieldResponse(state) }
        )
    }

    private fun setupSpinner() {
        massUnit.add(KG_UNIT)
        massUnit.add(YEN_UNIT)
        massUnit.add(TA_UNIT)
        massUnit.add(TAN_UNIT)
        val spMassUnitAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            massUnit
        )
        binding.spMassUnit.adapter = spMassUnitAdapter
    }

    private fun processFieldResponse(state: ScreenState<ArrayList<FieldApiResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    for (field in state.data) {
                        yieldHashMap.set(field.cropsName, field.estimateYield)
                        cropsHashMap.set(field.cropsName, field.id)
                        priceHashMap.set(field.cropsName, field.estimatePrice)
                        cropsList.add(field.cropsName)
                    }
                    val spCropsAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        cropsList
                    )
                    binding.spCropsDialog.adapter = spCropsAdapter
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
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()

                    createNotification()
                    showNotification()

                    showSnackbar(getString(R.string.create_cooperation))
                    dialog?.dismiss()
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
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "channel_name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =  NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(){
        val expiredDate = Utils.getExpireDate()
        val builder = NotificationCompat.Builder(requireActivity(), Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Đặt đơn hàng thành công")
            .setContentText("Bạn hãy thực hiện thanh toán đơn trước ${expiredDate}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(MainActivity.pendingIntent)
            .build()

        with(NotificationManagerCompat.from(requireActivity())){
            if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                return
            notify(0, builder)
        }
    }
}