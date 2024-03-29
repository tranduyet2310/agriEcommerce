package com.example.argiecommerce.view.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.InfoDialogAdapter
import com.example.argiecommerce.databinding.FragmentCropsDialogBinding
import com.example.argiecommerce.model.InfoDialog


class CropsDialogFragment : Fragment() {
    private lateinit var binding: FragmentCropsDialogBinding
    private lateinit var navController: NavController

    private lateinit var infoDialogAdapter: InfoDialogAdapter
    private val infoDialogList: ArrayList<InfoDialog> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentCropsDialogBinding.inflate(inflater)

        infoDialogList.add(InfoDialog("Nguyễn Văn A", "Táo", "Chấp nhận"))
        infoDialogList.add(InfoDialog("Nguyễn Văn A", "Xoài", "Đang chờ"))
        infoDialogList.add(InfoDialog("Nguyễn Văn A", "Cam", "Đã hủy"))

        infoDialogAdapter = InfoDialogAdapter(infoDialogList)

        binding.rcvCultivation.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = infoDialogAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun showImagePlaceholder() {
        binding.apply {
            imgPlaceholder.visibility = View.VISIBLE
            rcvCultivation.visibility = View.INVISIBLE
            headerLayout.visibility = View.GONE
        }
    }

    private fun hideImagePlaceholder() {
        binding.apply {
            imgPlaceholder.visibility = View.GONE
            rcvCultivation.visibility = View.VISIBLE
            headerLayout.visibility = View.VISIBLE
        }
    }
}