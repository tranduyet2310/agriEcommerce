package com.example.argiecommerce.view.supplier

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
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CropsAdapter
import com.example.argiecommerce.adapter.CooperationAdapter
import com.example.argiecommerce.databinding.FragmentCropsDialogBinding
import com.example.argiecommerce.model.CooperationResponse
import com.example.argiecommerce.model.CropsInfo
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.model.User
import com.example.argiecommerce.network.Api
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.CooperationViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
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
    private val cooperationViewModel: CooperationViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CooperationViewModel::class.java)
    }
    private val apiService: Api by lazy {
        RetrofitClient.getInstance().getApi()
    }

    private var user: User? = null
    private var supplierBasicInfo: SupplierBasicInfo? = null
    private lateinit var alertDialog: AlertDialog

    private lateinit var cooperationAdapter: CooperationAdapter
    private lateinit var cropsTotalAdapter: CropsAdapter
    private lateinit var cropsReceiveAdapter: CropsAdapter
    private val cooperationList: ArrayList<CooperationResponse> = arrayListOf()
    private val cropsTotalList: ArrayList<CropsInfo> = arrayListOf()
    private val cropsReceiveList: ArrayList<CropsInfo> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropsDialogBinding.inflate(inflater)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.culture)

        user = userViewModel.user
        supplierBasicInfo = userViewModel.supplierBasicInfo

        setupRecyclerView()
        lifecycleScope.launch {
            getCropsField()
            delay(500)
            getCropsReceived()
            binding.rcvCropsReceive.adapter?.notifyDataSetChanged()
        }
        getCooperationData()

        return binding.root
    }

    private fun getCooperationData() {
        cooperationViewModel.getCooperationBySupplierId(supplierBasicInfo!!.supplierId).observe(
            requireActivity(), { state -> processCooperationResponse(state) }
        )
    }

    suspend fun getCropsReceived() {
        cropsReceiveList.map { crops ->
            val fieldId = crops.id
            lifecycleScope.async(Dispatchers.IO) {
                val response = apiService.calculateCurrentTotal(fieldId, supplierBasicInfo!!.supplierId)
                if (response.isSuccessful) {
                    crops.estimateYield = response.body()?.message?.toDouble()!!
                }
            }
        }.awaitAll()
    }

    suspend fun getCropsField() {
        withContext(Dispatchers.IO) {
            val response = apiService.getCropsField(supplierBasicInfo!!.supplierId)
            if (response.isSuccessful) {
                val fields = response.body()
                if (fields != null) {
                    cropsTotalList.clear()
                    cropsReceiveList.clear()
                    for (field in fields) {
                        val cropsName = field.cropsName
                        val estimateYield = field.estimateYield
                        val cropsInfo = CropsInfo(cropsName, estimateYield)
                        cropsInfo.id = field.id

                        cropsTotalList.add(cropsInfo)
                        cropsReceiveList.add(cropsInfo)
                    }
                }
            }
        }
        withContext(Dispatchers.Main) {
            binding.rcvCropsTotal.adapter?.notifyDataSetChanged()
        }
    }


    private fun setupRecyclerView() {
        cooperationAdapter = CooperationAdapter(cooperationList)
        binding.rcvCultivation.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = cooperationAdapter
        }

        cropsTotalAdapter = CropsAdapter(cropsTotalList)
        binding.rcvCropsTotal.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = cropsTotalAdapter
        }

        cropsReceiveAdapter = CropsAdapter(cropsReceiveList)
        binding.rcvCropsReceive.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = cropsReceiveAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun showImagePlaceholder() {
        binding.apply {
            imgPlaceholder.visibility = View.VISIBLE
            rcvCultivation.visibility = View.GONE
            headerLayout.visibility = View.GONE
            tvPlaceholder.visibility = View.VISIBLE
        }
    }

    private fun hideImagePlaceholder() {
        binding.apply {
            imgPlaceholder.visibility = View.GONE
            rcvCultivation.visibility = View.VISIBLE
            headerLayout.visibility = View.VISIBLE
            tvPlaceholder.visibility = View.GONE
        }
    }

    private fun processCooperationResponse(state: ScreenState<ArrayList<CooperationResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    if (state.data.isEmpty()){
                        showImagePlaceholder()
                    } else {
                        hideImagePlaceholder()
                        cooperationList.clear()
                        cooperationList.addAll(state.data)
                        cooperationAdapter.notifyDataSetChanged()
                    }
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