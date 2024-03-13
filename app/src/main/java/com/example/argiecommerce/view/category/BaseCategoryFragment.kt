package com.example.argiecommerce.view.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.SubcategoryAdapter
import com.example.argiecommerce.databinding.FragmentBaseCategoryBinding


open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {

    private lateinit var binding: FragmentBaseCategoryBinding
    protected val demoAdapter: SubcategoryAdapter by lazy { SubcategoryAdapter(createSampleData()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseCategoryBinding.inflate(inflater)

        binding.rvCategory.apply {
            layoutManager = GridLayoutManager(
                requireContext(), 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = demoAdapter
            setHasFixedSize(true)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        demoAdapter.onClick = {
            val b = Bundle().apply { putString("category", it) }
            findNavController().navigate(R.id.action_standardFragment_to_detailsFragment, b)
        }
    }

    fun createSampleData(): ArrayList<String> {
        val dataList = arrayListOf<String>()
        dataList.add("Cây họ đỗ")
        dataList.add("Họ bắp cải")
        dataList.add("Cải")
        return dataList
    }

}