package com.example.argiecommerce.view.standard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.SpecialProductAdapter
import com.example.argiecommerce.databinding.FragmentBaseStandardBinding
import com.example.argiecommerce.model.Product


open class BaseStandardFragment : Fragment(R.layout.fragment_base_standard) {

    private lateinit var binding: FragmentBaseStandardBinding
    protected val demoAdapter: SpecialProductAdapter by lazy { SpecialProductAdapter(createSampleData()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseStandardBinding.inflate(inflater)

        binding.allProductRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2 ,GridLayoutManager.VERTICAL, false)
            adapter = demoAdapter
            setHasFixedSize(true)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        demoAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product_value", it) }
            findNavController().navigate(R.id.action_standardFragment_to_detailsFragment, b)
        }
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