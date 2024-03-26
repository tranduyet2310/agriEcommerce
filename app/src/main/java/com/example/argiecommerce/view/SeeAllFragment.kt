package com.example.argiecommerce.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.ProductLoadingAdapter
import com.example.argiecommerce.adapter.VerticalProductAdapter
import com.example.argiecommerce.databinding.FragmentSeeAllBinding
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants.FLASH_SALE
import com.example.argiecommerce.utils.Constants.PRODUCT_KEY
import com.example.argiecommerce.utils.Constants.RECENT_PRODUCT
import com.example.argiecommerce.viewmodel.ProductViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SeeAllFragment : Fragment() {
    private var _binding: FragmentSeeAllBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)
    }
    private val productAdapter: VerticalProductAdapter by lazy {
        VerticalProductAdapter(requireContext(), user)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    private var user: User? = null
    val args: SeeAllFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeeAllBinding.inflate(inflater, container, false)

        user = userViewModel.user
        val category = args.category
        val subcategory = args.subcategory
        val title = args.title

        binding.titleCategory.text = title

        setupRecyclerView()

        if (category != null) {
            getProductByCategory(category.id.toLong())
        } else if (subcategory != null) {
            getProductBySubcateogry(subcategory.id.toLong())
        } else if (title.equals(FLASH_SALE)){
            getProductsWithDiscount()
        }  else if(title.equals(RECENT_PRODUCT)){
            getUpcomingProducts()
        }

        return binding.root
    }

    private fun getUpcomingProducts() {
        val productApiRequest = ProductApiRequest()
        lifecycleScope.launch {
            productViewModel.getUpcomingProducts(productApiRequest).collectLatest { pagingData ->
                productAdapter.addLoadStateListener { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        productAdapter.itemCount < 1
                    ) {
                        binding.notFoundLayout.visibility = View.VISIBLE
                        binding.seeAllProductRecyclerView.visibility = View.INVISIBLE
                    } else {
                        binding.notFoundLayout.visibility = View.GONE
                        binding.seeAllProductRecyclerView.visibility = View.VISIBLE
                    }
                }
                productAdapter.submitData(pagingData)
            }
        }
    }

    private fun getProductsWithDiscount() {
        val productApiRequest = ProductApiRequest()
        lifecycleScope.launch {
            productViewModel.getProductsWithDiscount(productApiRequest).collectLatest { pagingData ->
                productAdapter.addLoadStateListener { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        productAdapter.itemCount < 1
                    ) {
                        binding.notFoundLayout.visibility = View.VISIBLE
                        binding.seeAllProductRecyclerView.visibility = View.INVISIBLE
                    } else {
                        binding.notFoundLayout.visibility = View.GONE
                        binding.seeAllProductRecyclerView.visibility = View.VISIBLE
                    }
                }
                productAdapter.submitData(pagingData)
            }
        }
    }

    private fun getProductBySubcateogry(subcategoryId: Long) {
        val productApiRequest = ProductApiRequest(subcategoryId)
        lifecycleScope.launch {
            productViewModel.getProductBySubCategory(productApiRequest).collectLatest { pagingData ->
                productAdapter.addLoadStateListener { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        productAdapter.itemCount < 1
                    ) {
                        binding.notFoundLayout.visibility = View.VISIBLE
                        binding.seeAllProductRecyclerView.visibility = View.INVISIBLE
                    } else {
                        binding.notFoundLayout.visibility = View.GONE
                        binding.seeAllProductRecyclerView.visibility = View.VISIBLE
                    }
                }
                productAdapter.submitData(pagingData)
            }
        }
    }

    private fun getProductByCategory(categoryId: Long) {
        val productApiRequest = ProductApiRequest(categoryId)
        lifecycleScope.launch {
            productViewModel.getProducts(productApiRequest).collectLatest { pagingData ->
                productAdapter.addLoadStateListener { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        productAdapter.itemCount < 1
                    ) {
                        binding.notFoundLayout.visibility = View.VISIBLE
                        binding.seeAllProductRecyclerView.visibility = View.INVISIBLE
                    } else {
                        binding.notFoundLayout.visibility = View.GONE
                        binding.seeAllProductRecyclerView.visibility = View.VISIBLE
                    }
                }
                productAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.seeAllProductRecyclerView.apply {
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
        navController = Navigation.findNavController(view)

        productAdapter.onClick = {
            val b = Bundle().apply { putParcelable(PRODUCT_KEY, it) }
            findNavController().navigate(R.id.action_seeAllFragment_to_detailsFragment, b)
        }

        binding.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}