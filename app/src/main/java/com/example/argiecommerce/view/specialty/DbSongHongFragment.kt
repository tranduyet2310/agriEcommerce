package com.example.argiecommerce.view.specialty

import android.animation.LayoutTransition
import android.app.AlertDialog
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
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.FavoriteResponse
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.CartViewModel
import com.example.argiecommerce.viewmodel.FavoriteViewModel
import com.example.argiecommerce.viewmodel.ProductViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DbSongHongFragment : Fragment() {
    private lateinit var binding: FragmentBaseSpecialtyBinding

    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)
    }
    private val productAdapter: VerticalProductAdapter by lazy {
        VerticalProductAdapter(requireContext(), user)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }
    private val favoriteViewModel: FavoriteViewModel by lazy {
        ViewModelProvider(requireActivity()).get(FavoriteViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }

    private lateinit var alertDialog: AlertDialog

    private var user: User? = null
    private var specialtyCategory: CategoryApiResponse? = null
    private var dbSongHongId: Long = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseSpecialtyBinding.inflate(inflater)

        user = userViewModel.user
        specialtyCategory = userViewModel.category
        setupRecyclerView()
        setupAreaLayout()
        setupAreaData()
        if (specialtyCategory != null){
            getProducts()
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter.onClick = {
            val b = Bundle().apply { putParcelable(Constants.PRODUCT_KEY, it) }
            findNavController().navigate(R.id.action_specialtyFragment_to_detailsFragment, b)
        }

        productAdapter.onCartClick = {
            addToCart(it)
        }

        productAdapter.onFavouriteClick = {
            addFavoriteProduct(it)
        }

        productAdapter.onShareClick = {
            shareProduct(it)
        }
    }

    private fun getProducts() {
        for (subcategory in specialtyCategory!!.subCategoryList){
            if (subcategory.subcategoryName.equals(Constants.DBSH)){
                dbSongHongId = subcategory.id.toLong()
            }
        }

        val productApiRequest = ProductApiRequest(dbSongHongId)
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
        binding.titleArea.text = requireContext().resources.getString(R.string.db_song_hong_name)
        binding.imageArea.setImageResource(R.drawable.db_song_hong)
        binding.areaInfo.text = requireContext().resources.getString(R.string.db_song_hong)
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

    private fun addFavoriteProduct(product: Product) {
        if (product.isFavourite == 1) {
            val token = loginUtils.getUserToken()
            favoriteViewModel.createFavoriteProduct(token, user!!.id, product.productId).observe(
                requireActivity(), { state -> processFavProductResponse(state) }
            )
        }
    }

    private fun addToCart(product: Product) {
        if(product.isNew){
            showSnackbar(getString(R.string.not_sale))
            return
        } else if (product.productQuantity == 0){
            showSnackbar(getString(R.string.no_product_left))
            return
        }
        if (product.isInCart == 1) {
            val token = loginUtils.getUserToken()
            cartViewModel.addToCart(token, user!!.id, product.productId).observe(
                requireActivity(), {state -> processCartResponse(state)}
            )
        }
    }

    private fun shareProduct(product: Product){
        showSnackbar(getString(R.string.share_successfully))
    }
    private fun processFavProductResponse(state: ScreenState<FavoriteResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    Snackbar.make(requireView(), requireContext().resources.getString(R.string.add_favourite_product), Snackbar.LENGTH_SHORT).show()
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    showSnackbar(state.message)
                }
            }
        }
    }

    private fun processCartResponse(state: ScreenState<CartResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    Snackbar.make(requireView(), requireContext().resources.getString(R.string.add_into_cart), Snackbar.LENGTH_SHORT).show()
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    showSnackbar(state.message)
                }
            }
        }
    }
    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
    }
}