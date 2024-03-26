package com.example.argiecommerce.view.specialty

import android.animation.LayoutTransition
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
import com.example.argiecommerce.databinding.FragmentBaseSpecialtyBinding
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DbSongCuuLongFragment : Fragment()  {
    private lateinit var binding: FragmentBaseSpecialtyBinding

    private val productAdapter: VerticalProductAdapter by lazy {
        VerticalProductAdapter(requireContext())
    }
    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseSpecialtyBinding.inflate(inflater)

        setupRecyclerView()
        setupAreaLayout()
        setupAreaData()
        getProducts()

        return binding.root
    }

    private fun getProducts() {
        val productApiRequest = ProductApiRequest(3)
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
    private fun setupAreaData() {
        binding.titleArea.text = requireContext().resources.getString(R.string.db_song_cuu_long_name)
        binding.imageArea.setImageResource(R.drawable.db_song_cl)
        binding.areaInfo.text = requireContext().resources.getString(R.string.db_song_cuu_long)
    }

    private fun setupAreaLayout() {
        binding.layouts.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.expandable.setOnClickListener {
            val visibility =
                if (binding.imageArea.visibility == View.GONE) View.VISIBLE else View.GONE
            binding.imageArea.visibility = visibility
            binding.scrollAreaInfo.visibility = visibility
            binding.titleProductInArea.visibility = visibility
            binding.scrollProductArea.visibility = visibility
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter.onClick = {
            val b = Bundle().apply { putParcelable(Constants.PRODUCT_KEY, it) }
            findNavController().navigate(R.id.action_specialtyFragment_to_detailsFragment, b)
        }
    }
}