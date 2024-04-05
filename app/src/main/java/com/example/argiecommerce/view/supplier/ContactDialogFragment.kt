package com.example.argiecommerce.view.supplier

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.navigation.fragment.findNavController
import com.example.argiecommerce.databinding.FragmentContactDialogBinding
import com.example.argiecommerce.utils.Constants.KG_UNIT
import com.example.argiecommerce.utils.Constants.TAN_UNIT
import com.example.argiecommerce.utils.Constants.TA_UNIT
import com.example.argiecommerce.utils.Constants.YEN_UNIT


class ContactDialogFragment : AppCompatDialogFragment() {
    private lateinit var binding: FragmentContactDialogBinding

    private val massUnit: ArrayList<String> = arrayListOf()
    private var coefficient: Int = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactDialogBinding.inflate(inflater, container, false)

        setupSpinner()

        return binding.root
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("TEST", "in ContactDialogFragment - nothing to show")
            }
        }

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        binding.btnSave.setOnClickListener {
            dialog?.dismiss()
        }

        binding.policy.setOnClickListener {
            val action =
                ContactDialogFragmentDirections.actionContactDialogFragmentToPolicyFragment()
            this@ContactDialogFragment.findNavController().navigate(action)
        }
    }

}