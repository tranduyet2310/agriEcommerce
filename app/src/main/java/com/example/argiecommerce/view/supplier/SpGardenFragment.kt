package com.example.argiecommerce.view.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentGardenBinding
import com.example.argiecommerce.model.FieldApiResponse
import com.example.argiecommerce.model.FieldDetail
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.utils.Constants.LONG_TERM_PLANT
import com.example.argiecommerce.utils.Constants.SHORT_TERM_PLANT
import com.example.argiecommerce.utils.CropsStatus
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils
import com.example.argiecommerce.viewmodel.SupplierViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class SpGardenFragment : Fragment() {
    private lateinit var binding: FragmentGardenBinding
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val supplierViewModel: SupplierViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SupplierViewModel::class.java)
    }

    private var supplierBasicInfo: SupplierBasicInfo? = null
    private var cropsNameList: ArrayList<String> = arrayListOf()
    private lateinit var fieldInfoList: ArrayList<FieldApiResponse>
    private lateinit var detailInfoList: ArrayList<FieldDetail>
    private lateinit var cropsStatus: HashMap<String, Int>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGardenBinding.inflate(inflater)

        supplierBasicInfo = userViewModel.supplierBasicInfo
        setupSpinner()
        if (supplierBasicInfo != null) getData()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinnerCropsListener()
    }

    private fun setupSpinnerCropsListener() {
        binding.spinnerCrops.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val value = cropsNameList[position]
                for (field in fieldInfoList) {
                    if (field.cropsName.equals(value)) {
                        binding.tvSeason.text = field.season
                        binding.tvLandArea.text = field.area
                        if (field.cropsType.equals(LONG_TERM_PLANT)) {
                            val status = cropsStatus.get(value)
                            if (status != null) {
                                val cropState = statusForLongTermPlant(status)
                                setUpColorStateLong(cropState)
                            }

                            detailInfoList = field.fieldDetails
                            for (detail in detailInfoList) {
                                setupFieldDetailLong(detail.cropsStatus, detail.details, detail.dateCreated, field.estimateYield)
                            }
                        } else if (field.cropsType.equals(SHORT_TERM_PLANT)) {
                            val status = cropsStatus.get(value)
                            if (status != null) {
                                val cropState = statusForShortTermPlant(status)
                                setUpColorStateShort(cropState)
                            }

                            detailInfoList = field.fieldDetails
                            for (detail in detailInfoList) {
                                setupFieldDetailShort(detail.cropsStatus, detail.details, detail.dateCreated, field.estimateYield)
                            }
                        }
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showSnackbar("Nothing to show")
            }
        }
    }

    private fun setupSpinner() {
        val spCropsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, cropsNameList)
        binding.spinnerCrops.adapter = spCropsAdapter
    }

    private fun getData() {
        supplierViewModel.getFieldInfo(supplierBasicInfo!!.supplierId).observe(
            requireActivity(), { state -> processFieldResponse(state) }
        )
    }

    private fun setUpColorStateShort(currentState: Int) {
        binding.scrollLongTermPlant.visibility = View.GONE
        binding.scrollShortTermPlant.visibility = View.VISIBLE

        if (currentState > 5 || currentState < 0) return
        val listCircle: ArrayList<View> = arrayListOf()
        val listLine: ArrayList<View> = arrayListOf()

        listCircle.add(binding.viewLand)
        listCircle.add(binding.viewSeed)
        listCircle.add(binding.viewGermination)
        listCircle.add(binding.viewTakeCare)
        listCircle.add(binding.viewChemical)
        listCircle.add(binding.viewHarvest)

        listLine.add(binding.lineLandSeed)
        listLine.add(binding.lineSeedGermination)
        listLine.add(binding.lineGerminationTakeCare)
        listLine.add(binding.lineTakeCareChemical)
        listLine.add(binding.lineChemicalHarvest)

        for (i in 0 until 6) {
            if (i < currentState) {
                listCircle[i].setBackgroundResource(R.drawable.shape_garden_status_completed)
                if (i >= 1)
                    listLine[i].setBackgroundColor(getResources().getColor(R.color.greenAgri))
            } else if (i == currentState) {
                listCircle[i].setBackgroundResource(R.drawable.shape_garden_status_current)
            } else {
                listCircle[i].setBackgroundResource(R.drawable.shape_garden_status_remain)
            }

        }

    }

    private fun setUpColorStateLong(currentState: Int) {
        binding.scrollLongTermPlant.visibility = View.VISIBLE
        binding.scrollShortTermPlant.visibility = View.GONE

        if (currentState > 4 || currentState < 0) return
        val listCircle: ArrayList<View> = arrayListOf()
        val listLine: ArrayList<View> = arrayListOf()

        listCircle.add(binding.viewTakeCareLong)
        listCircle.add(binding.viewFloweringLong)
        listCircle.add(binding.viewChemicalLong)
        listCircle.add(binding.viewFruitingLong)
        listCircle.add(binding.viewHarvestLong)

        listLine.add(binding.lineTakeCareFloweringLong)
        listLine.add(binding.lineFloweringChemicalLong)
        listLine.add(binding.lineChemicalFruitingLong)
        listLine.add(binding.lineHarvestFruitingLong)

        for (i in 0 until 5) {
            if (i < currentState) {
                listCircle[i].setBackgroundResource(R.drawable.shape_garden_status_completed)
                if (i >= 1)
                    listLine[i].setBackgroundColor(getResources().getColor(R.color.greenAgri))
            } else if (i == currentState) {
                listCircle[i].setBackgroundResource(R.drawable.shape_garden_status_current)
            } else {
                listCircle[i].setBackgroundResource(R.drawable.shape_garden_status_remain)
            }

        }

    }

    private fun setupFieldDetailShort(status: CropsStatus, detail: String, date: String, yield: Double) {
        when (status) {
            CropsStatus.MAKE_LAND -> {
                binding.tvLandContent.text = detail
                binding.tvLandDate.text = date
            }

            CropsStatus.SOW_SEED -> {
                binding.tvSeedContent.text = detail
                binding.tvSeedDate.text = date
            }

            CropsStatus.GERMINATION -> {
                binding.tvGerminationContent.text = detail
                binding.tvGerminationDate.text = date
            }

            CropsStatus.TAKE_CARE -> {
                binding.tvTakeCareContent.text = detail
                binding.tvTakeCareDate.text = date
            }

            CropsStatus.PEST_PREVENTION -> {
                binding.tvChemicalContent.text = detail
                binding.tvChemicalDate.text = date
            }

            CropsStatus.HARVEST -> {
                binding.tvHarvestYield.visibility = View.VISIBLE
                binding.tvHarvestDetail.visibility = View.VISIBLE
                binding.titleHarvestYield.visibility = View.VISIBLE
                binding.tvHarvestYield.text = Utils.formatYield(yield)
                binding.tvHarvestDate.text = date
                binding.tvHarvestDetail.text = detail
            }

            else -> {}
        }
    }

    private fun setupFieldDetailLong(status: CropsStatus, detail: String, date: String, yield: Double) {
        when (status) {
            CropsStatus.TAKE_CARE -> {
                binding.tvTakeCareContentLong.text = detail
                binding.tvTakeCareDateLong.text = date
            }

            CropsStatus.FLOWERING -> {
                binding.tvFloweringContentLong.text = detail
                binding.tvFloweringDateLong.text = date
            }

            CropsStatus.PEST_PREVENTION -> {
                binding.tvChemicalContentLong.text = detail
                binding.tvChemicalDateLong.text = date
            }

            CropsStatus.FRUITING -> {
                binding.tvFruitingContentLong.text = detail
                binding.tvFruitingDateLong.text = date
            }

            CropsStatus.HARVEST -> {
                binding.tvHarvestYieldLong.visibility = View.VISIBLE
                binding.tvHarvestDetailLong.visibility = View.VISIBLE
                binding.titleHarvestYieldLong.visibility = View.VISIBLE
                binding.tvHarvestYieldLong.text = Utils.formatYield(yield)
                binding.tvHarvestDateLong.text = date
                binding.tvHarvestDetailLong.text = detail
            }

            else -> {}
        }
    }

    private fun processFieldResponse(state: ScreenState<ArrayList<FieldApiResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    binding.progressBar.visibility = View.GONE
                    fieldInfoList = state.data
                    if (fieldInfoList.size == 0){
                        showSnackbar(getString(R.string.garden_info_empty))
                    } else {
                        cropsNameList.clear()
                        cropsStatus = hashMapOf()

                        for (field in fieldInfoList) {
                            var currentState: Int = 0
                            cropsNameList.add(field.cropsName)
                            detailInfoList = field.fieldDetails
                            for (detail in detailInfoList) {
                                currentState = maxOf(currentState, detail.cropsStatus.value)
                            }
                            cropsStatus.set(field.cropsName, currentState)
                        }

                        setupSpinner()
                        binding.tvSeason.text = fieldInfoList[0].season
                        binding.tvLandArea.text = fieldInfoList[0].area
                    }
                }
            }

            is ScreenState.Error -> {
                binding.progressBar.visibility = View.GONE
                if (state.message != null) {
                    showSnackbar(state.message)
                }
            }
        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
    }

    private fun statusForLongTermPlant(value: Int): Int {
        return when (value) {
            CropsStatus.TAKE_CARE.value -> 0
            CropsStatus.FLOWERING.value -> 1
            CropsStatus.PEST_PREVENTION.value -> 2
            CropsStatus.FRUITING.value -> 3
            CropsStatus.HARVEST.value -> 4
            else -> 0
        }
    }

    private fun statusForShortTermPlant(value: Int): Int {
        return when (value) {
            CropsStatus.MAKE_LAND.value -> 0
            CropsStatus.SOW_SEED.value -> 1
            CropsStatus.GERMINATION.value -> 2
            CropsStatus.TAKE_CARE.value -> 3
            CropsStatus.PEST_PREVENTION.value -> 4
            CropsStatus.HARVEST.value -> 5
            else -> 0
        }
    }
}