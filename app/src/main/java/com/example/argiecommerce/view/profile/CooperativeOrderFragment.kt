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
import com.example.argiecommerce.adapter.CooperativeOrderAdapter
import com.example.argiecommerce.adapter.OrderLoadingAdapter
import com.example.argiecommerce.databinding.FragmentCooperativeOrderBinding
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.viewmodel.CooperativeOrderViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CooperativeOrderFragment : Fragment() {
    private lateinit var binding: FragmentCooperativeOrderBinding
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val cooperativeOrderAdapter: CooperativeOrderAdapter by lazy {
        CooperativeOrderAdapter()
    }
    private val cooperativeOrderViewModel: CooperativeOrderViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CooperativeOrderViewModel::class.java)
    }
    private var user: User? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCooperativeOrderBinding.inflate(inflater)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.all_orders)

        user = userViewModel.user
        setupRecyclerView()
        if (user != null) getCooperativeOrder()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        cooperativeOrderAdapter.onClick = {
            val b = Bundle().apply {
                putLong(Constants.COOPERATIIVE_KEY, it.id)
            }
            navController.navigate(R.id.action_cooperativeOrderFragment_to_cooperativeDetailFragment, b)
        }
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
            adapter = cooperativeOrderAdapter.withLoadStateHeaderAndFooter(
                header = OrderLoadingAdapter { cooperativeOrderAdapter.retry() },
                footer = OrderLoadingAdapter { cooperativeOrderAdapter.retry() }
            )
        }
    }

    private fun getCooperativeOrder() {
        lifecycleScope.launch {
            cooperativeOrderViewModel.getCooperativeOrderByUserId(user!!.id).collectLatest { pagingData ->
                cooperativeOrderAdapter.addLoadStateListener { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        cooperativeOrderAdapter.itemCount < 1
                    ) {
                        hideOrderList()
                    } else {
                        showOrderList()
                    }
                }
                cooperativeOrderAdapter.submitData(pagingData)
            }
        }
    }
}