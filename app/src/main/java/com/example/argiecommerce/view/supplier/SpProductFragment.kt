package com.example.argiecommerce.view.supplier

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.ProductLoadingAdapter
import com.example.argiecommerce.adapter.VerticalProductAdapter
import com.example.argiecommerce.databinding.FragmentSuppilerProductBinding
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.FavoriteResponse
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.view.home.HomeFragmentDirections
import com.example.argiecommerce.viewmodel.CartViewModel
import com.example.argiecommerce.viewmodel.FavoriteViewModel
import com.example.argiecommerce.viewmodel.SupplierViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SpProductFragment : Fragment() {
    private lateinit var binding: FragmentSuppilerProductBinding

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val supplierViewModel: SupplierViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SupplierViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }
    private val favoriteViewModel: FavoriteViewModel by lazy {
        ViewModelProvider(requireActivity()).get(FavoriteViewModel::class.java)
    }

    private lateinit var alertDialog: AlertDialog
    private var user: User? = null
    private var supplierBasicInfo: SupplierBasicInfo? = null
    private lateinit var productAdapter: VerticalProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuppilerProductBinding.inflate(inflater)

        supplierBasicInfo = userViewModel.supplierBasicInfo
        user = userViewModel.user
        setupRecyclerView()
        getSupplierProduct()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter.onCartClick = {
            addToCart(it)
        }
        productAdapter.onFavouriteClick = {
            addFavoriteProduct(it)
        }
        productAdapter.onShareClick = {
            shareProduct(it)
        }
        productAdapter.onClick = {
            goToDetailFragment(it)
        }
    }

    private fun goToDetailFragment(product: Product){
        val action = SupplierFragmentDirections.actionSuppilerFragmentToDetailsFragment(product)
        this@SpProductFragment.findNavController().navigate(action)
    }
    private fun shareProduct(product: Product){
        Snackbar.make(requireView(), "Chia sẻ thành công", Snackbar.LENGTH_SHORT).show()
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
        }
        if (product.isInCart == 1) {
            val token = loginUtils.getUserToken()
            cartViewModel.addToCart(token, user!!.id, product.productId).observe(
                requireActivity(), {state -> processCartResponse(state)}
            )
        }
    }
    private fun setupRecyclerView() {
        binding.allProductRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            productAdapter = VerticalProductAdapter(requireContext(), user)
            adapter = productAdapter.withLoadStateHeaderAndFooter(
                header = ProductLoadingAdapter { productAdapter.retry() },
                footer = ProductLoadingAdapter { productAdapter.retry() }
            )
        }
    }

    private fun getSupplierProduct() {
        lifecycleScope.launch {
            supplierViewModel.getSupplierProducts(supplierBasicInfo!!.supplierId).collectLatest { pagingData ->
                productAdapter.submitData(pagingData)
            }
        }
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