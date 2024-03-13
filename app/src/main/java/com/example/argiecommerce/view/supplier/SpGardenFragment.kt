package com.example.argiecommerce.view.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentGardenBinding


class SpGardenFragment : Fragment() {
    private lateinit var binding: FragmentGardenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGardenBinding.inflate(inflater)

        binding.tvSeason.text = "Đông - Xuân"
        binding.tvLandArea.text = "5 ha"

        val cropsValue = arrayOf("Rau cải", "Rau lang")
        val spCropsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, cropsValue)
        binding.spinnerCrops.adapter = spCropsAdapter

        val currentState = 4        // 0 - 5

        setUpColorState(currentState)


        return binding.root
    }

    private fun setUpColorState(currentState: Int) {
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

        for(i in 0 until 6){
            if (i < currentState){
                listCircle[i].setBackgroundResource(R.drawable.shape_garden_status_completed)
                if (i >= 1)
                    listLine[i].setBackgroundColor(getResources().getColor(R.color.greenAgri))
            }
            else if (i == currentState) {
                listCircle[i].setBackgroundResource(R.drawable.shape_garden_status_current)
            } else {
                listCircle[i].setBackgroundResource(R.drawable.shape_garden_status_remain)
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}