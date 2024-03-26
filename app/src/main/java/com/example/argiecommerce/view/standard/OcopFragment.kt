package com.example.argiecommerce.view.standard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.ProductLoadingAdapter
import com.example.argiecommerce.adapter.VerticalProductAdapter
import com.example.argiecommerce.databinding.FragmentBaseStandardBinding
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.viewmodel.ProductViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class OcopFragment : Fragment() {
    private lateinit var binding: FragmentBaseStandardBinding

    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)
    }
    protected val productAdapter: VerticalProductAdapter by lazy {
        VerticalProductAdapter(requireContext(), user)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseStandardBinding.inflate(inflater)

        user = userViewModel.user
        setupRecyclerView()
        getProducts()

        return binding.root
    }

    private fun getProducts() {
        val productApiRequest = ProductApiRequest(4)
        lifecycleScope.launch {
            productViewModel.getProductBySubCategory(productApiRequest)
                .collectLatest { pagingData ->
                    productAdapter.addLoadStateListener { loadState ->
                        if (loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached &&
                            productAdapter.itemCount < 1
                        ) {
                            binding.notFoundLayout.visibility = View.VISIBLE
                            binding.allProductRecyclerView.visibility = View.INVISIBLE
                        } else {
                            binding.notFoundLayout.visibility = View.GONE
                            binding.allProductRecyclerView.visibility = View.VISIBLE
                        }
                    }
                    productAdapter.submitData(pagingData)
                }
        }
    }

    private fun setupRecyclerView() {
        binding.allProductRecyclerView.apply {
            layoutManager = GridLayoutManager(
                requireContext(), 2,
                GridLayoutManager.VERTICAL, false
            )
            setHasFixedSize(true)
            adapter = productAdapter.withLoadStateHeaderAndFooter(
                header = ProductLoadingAdapter { productAdapter.retry() },
                footer = ProductLoadingAdapter { productAdapter.retry() }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter.onClick = {
            val b = Bundle().apply { putParcelable(Constants.PRODUCT_KEY, it) }
            findNavController().navigate(R.id.action_standardFragment_to_detailsFragment, b)
        }
    }
}