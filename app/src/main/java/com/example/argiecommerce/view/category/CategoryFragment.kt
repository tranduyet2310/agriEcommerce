package com.example.argiecommerce.view.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.adapter.SubcategoryAdapter
import com.example.argiecommerce.adapter.CategoryAdapter
import com.example.argiecommerce.databinding.FragmentCategoryBinding
import com.example.argiecommerce.model.CategoryItem
import com.example.argiecommerce.utils.ImageUtils
import com.example.argiecommerce.utils.Utils

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var subcategoryAdapter: SubcategoryAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryItemList: ArrayList<CategoryItem>
    private lateinit var categoryDetailList: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        getCategoryData()
        getCategoryDetailData()

        binding.rvCategoryItem.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = categoryAdapter
        }

        binding.rvCategoryDetail.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = subcategoryAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        categoryAdapter.onClick = {
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
        }

        subcategoryAdapter.onClick = {
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCategoryData() {
        val imageList = ImageUtils.getImages.getCategoryItem()
        val titleList = Utils.getText.getCategoryItemTitle()
        categoryItemList = arrayListOf()
        for (i in imageList.indices) {
            val categoryItem = CategoryItem(imageList[i], titleList[i])
            categoryItemList.add(categoryItem)
        }
        categoryAdapter = CategoryAdapter(categoryItemList)
    }

    private fun getCategoryDetailData() {
        categoryDetailList = arrayListOf()
        categoryDetailList.add("Cậy họ cải")
        categoryDetailList.add("Cậy họ đỗ")
        categoryDetailList.add("Hạt")
        categoryDetailList.add("Gạo")
        subcategoryAdapter = SubcategoryAdapter(categoryDetailList)
    }
}