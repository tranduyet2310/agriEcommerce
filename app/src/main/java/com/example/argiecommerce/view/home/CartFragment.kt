package com.example.argiecommerce.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CartProductAdapter
import com.example.argiecommerce.databinding.FragmentCartBinding
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.viewmodel.CartViewModel


class CartFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val cartAdapter: CartProductAdapter by lazy {
        CartProductAdapter()
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }
    private var totalPrice = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        setupRecyclerView()

        val data = cartViewModel.products

        if (data.isEmpty()){
            showEmptyCart()
            hideOtherViews()
        } else {
            hideEmptyCart()
            showOtherViews()
            cartAdapter.differ.submitList(data)
        }


        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.buttonCheckout.setOnClickListener(this);
        cartAdapter.onProductClick = {
            val b = Bundle().apply {
                putParcelable(Constants.PRODUCT_KEY, it.product)
            }
            navController.navigate(R.id.action_cartFragment_to_detailsFragment, b)
        }

        cartAdapter.onPlusClick = {

        }

        cartAdapter.onMinusClick = {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonCheckout -> goToBillingFragment()
        }
    }

    private fun goToBillingFragment() {
        navController.navigate(R.id.action_cartFragment_to_billingFragment)
    }

    private fun showOtherViews(){
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews(){
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun hideEmptyCart(){
        binding.apply {
            layoutCartEmpty.visibility = View.GONE
        }
    }

    private fun showEmptyCart(){
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
        }
    }
}