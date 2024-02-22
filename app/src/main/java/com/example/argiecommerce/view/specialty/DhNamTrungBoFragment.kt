package com.example.argiecommerce.view.specialty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.DemoAdapter
import com.example.argiecommerce.databinding.FragmentDbSongHongBinding
import com.example.argiecommerce.databinding.FragmentDhNamTrungBoBinding
import com.example.argiecommerce.model.Product


class DhNamTrungBoFragment : Fragment(), View.OnClickListener,
    DemoAdapter.DemoAdapterOnClickListener {
    private var _binding: FragmentDhNamTrungBoBinding? = null
    private val binding get() = _binding!!

    //    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<Product>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDhNamTrungBoBinding.inflate(inflater, container, false)

        recyclerView = binding.dhNTBRecyclerView
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        dataList = arrayListOf()
        getSampleData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    private fun getSampleData() {
        val demo0 = Product("Apple", 50000.00, 50, "HTX HN", "Vegetable")
        val demo1 = Product("Orange", 150000.00, 50, "HTX HN", "Vegetable")
        val demo2 = Product("Strawberry", 1050000.00, 50, "HTX HN", "Vegetable")
        val demo3 = Product("Coconut", 9550000.00, 50, "HTX HN", "Vegetable")

        demo0.productImage = R.drawable.product_demo.toString()
        demo1.productImage = R.drawable.product_demo.toString()
        demo2.productImage = R.drawable.product_demo.toString()
        demo3.productImage = R.drawable.product_demo.toString()
        dataList.add(demo0)
        dataList.add(demo1)
        dataList.add(demo2)
        dataList.add(demo3)

        binding.dhNTBRecyclerView.adapter = DemoAdapter(dataList, this)
    }

    override fun onClick(product: Product) {
        Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
    }
}