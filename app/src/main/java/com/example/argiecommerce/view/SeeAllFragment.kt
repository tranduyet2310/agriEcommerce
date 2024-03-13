package com.example.argiecommerce.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.SpecialProductAdapter
import com.example.argiecommerce.databinding.FragmentSeeAllBinding
import com.example.argiecommerce.model.Product


class SeeAllFragment : Fragment() {
    private var _binding: FragmentSeeAllBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val demoAdapter: SpecialProductAdapter by lazy { SpecialProductAdapter(createSampleData()) }

    val args: SeeAllFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeeAllBinding.inflate(inflater, container, false)

        val title: String = args.category

        binding.seeAllProductRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2 ,
                GridLayoutManager.VERTICAL, false)
            adapter = demoAdapter
            setHasFixedSize(true)
        }

        binding.titleCategory.text = title

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        demoAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product_value", it) }
            findNavController().navigate(R.id.action_seeAllFragment_to_detailsFragment, b)
        }

        binding.imgBack.setOnClickListener{
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun createSampleData(): ArrayList<Product>{
        val dataList = arrayListOf<Product>()
        val demo0 = Product("Apple", 50000.00, 50, "HTX HN", "Vegetable")
        val demo1 = Product("Orange", 150000.00, 50, "HTX HN", "Vegetable")
        val demo2 = Product("Strawberry", 1050000.00, 50, "HTX HN", "Vegetable")
        val demo3 = Product("Coconut", 9550000.00, 50, "HTX HN", "Vegetable")
        val demo4 = Product("Coconut", 9550000.00, 50, "HTX HN", "Vegetable")
        val demo5 = Product("Coconut", 9550000.00, 50, "HTX HN", "Vegetable")

        demo0.productImage = R.drawable.product_demo.toString()
        demo1.productImage = R.drawable.viewfilpper_1.toString()
        demo2.productImage = R.drawable.viewfilpper_2.toString()
        demo3.productImage = R.drawable.viewfilpper_3.toString()
        demo4.productImage = R.drawable.viewfilpper_4.toString()
        demo5.productImage = R.drawable.viewfilpper_5.toString()
        dataList.add(demo0)
        dataList.add(demo1)
        dataList.add(demo2)
        dataList.add(demo3)
        dataList.add(demo4)
        dataList.add(demo5)
        return dataList
    }
}