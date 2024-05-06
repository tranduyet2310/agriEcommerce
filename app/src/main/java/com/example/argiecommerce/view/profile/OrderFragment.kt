package com.example.argiecommerce.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.OrderAdapter
import com.example.argiecommerce.adapter.OrderLoadingAdapter
import com.example.argiecommerce.databinding.FragmentOrderBinding
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants.ORDER_KEY
import com.example.argiecommerce.viewmodel.OrderViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class OrderFragment : Fragment(){
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider(requireActivity()).get(OrderViewModel::class.java)
    }
    private val orderAdapter: OrderAdapter by lazy {
        OrderAdapter()
    }

    private var user: User? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.all_orders)

        user = userViewModel.user
        setupRecyclerView()
        if (user != null) getOrders()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        orderAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable(ORDER_KEY, it)
            }
            navController.navigate(R.id.action_orderFragment_to_orderDetailFragment, b)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showOrderList(){
        binding.emptyBox.visibility = View.GONE
        binding.tvEmptyOrders.visibility = View.GONE
        binding.noBookmarks.visibility = View.GONE
        binding.rvAllOrders.visibility = View.VISIBLE
    }

    private fun hideOrderList(){
        binding.emptyBox.visibility = View.VISIBLE
        binding.tvEmptyOrders.visibility = View.VISIBLE
        binding.noBookmarks.visibility = View.VISIBLE
        binding.rvAllOrders.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        binding.rvAllOrders.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = orderAdapter.withLoadStateHeaderAndFooter(
                header = OrderLoadingAdapter { orderAdapter.retry() },
                footer = OrderLoadingAdapter { orderAdapter.retry() }
            )
        }
    }

    private fun getOrders() {
        lifecycleScope.launch {
            orderViewModel.getOrderByUserId(user!!.id).collectLatest { pagingData ->
                orderAdapter.addLoadStateListener { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        orderAdapter.itemCount < 1
                    ) {
                        hideOrderList()
                    } else {
                        showOrderList()
                    }
                }
                orderAdapter.submitData(pagingData)
            }
        }
    }
}