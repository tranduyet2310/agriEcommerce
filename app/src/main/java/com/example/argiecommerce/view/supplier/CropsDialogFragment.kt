package com.example.argiecommerce.view.supplier

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CropsAdapter
import com.example.argiecommerce.databinding.FragmentCropsDialogBinding
import com.example.argiecommerce.model.CropsInfo
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.model.User
import com.example.argiecommerce.network.Api
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CropsDialogFragment : Fragment() {
    private lateinit var binding: FragmentCropsDialogBinding
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val apiService: Api by lazy {
        RetrofitClient.getInstance().getApi()
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }

    private var user: User? = null
    private var supplierBasicInfo: SupplierBasicInfo? = null
    private lateinit var alertDialog: AlertDialog
    private lateinit var cropsReceiveAdapter: CropsAdapter
    private val cropsReceiveList: ArrayList<CropsInfo> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropsDialogBinding.inflate(inflater)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.culture)

        user = userViewModel.user
        supplierBasicInfo = userViewModel.supplierBasicInfo

        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }
            getCropsField()
            delay(500)
            getCropsReceived()
            delay(500)
            withContext(Dispatchers.Main){
                alertDialog.dismiss()
                showInfo()
            }
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }
    private fun showInfo() {
        if (cropsReceiveList.isEmpty()){
            showImagePlaceholder()
        } else {
            hideImagePlaceholder()
            setupRecyclerView()
        }
    }

    suspend fun getCropsReceived() {
        withContext(Dispatchers.IO){
            cropsReceiveList.map { crops ->
                val fieldId = crops.id
                lifecycleScope.async(Dispatchers.IO) {
                    val response =
                        apiService.calculateCurrentTotal(fieldId, supplierBasicInfo!!.supplierId)
                    if (response.isSuccessful) {
                        crops.currentYield = response.body()?.message?.toDouble()!!
                    }
                }
            }.awaitAll()
        }
    }

    suspend fun getCropsField() {
        withContext(Dispatchers.IO) {
            val response = apiService.getCropsField(supplierBasicInfo!!.supplierId)
            if (response.isSuccessful) {
                val fields = response.body()
                if (fields != null) {
                    cropsReceiveList.clear()
                    for (field in fields) {
                        val cropsName = field.cropsName
                        val estimateYield = field.estimateYield
                        val cropsInfo = CropsInfo(cropsName, estimateYield)
                        cropsInfo.id = field.id
                        cropsInfo.estimatePrice = field.estimatePrice
                        cropsInfo.currentYield = 0.0

                        cropsReceiveList.add(cropsInfo)
                    }
                }
            }
        }
    }


    private fun setupRecyclerView() {
        cropsReceiveAdapter = CropsAdapter(cropsReceiveList)
        binding.rcvCultivation.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = cropsReceiveAdapter
        }
    }

    private fun showImagePlaceholder() {
        binding.apply {
            imgPlaceholder.visibility = View.VISIBLE
            rcvCultivation.visibility = View.GONE
            tvPlaceholder.visibility = View.VISIBLE
        }
    }

    private fun hideImagePlaceholder() {
        binding.apply {
            imgPlaceholder.visibility = View.GONE
            rcvCultivation.visibility = View.VISIBLE
            tvPlaceholder.visibility = View.GONE
        }
    }
}