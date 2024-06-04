package com.example.argiecommerce.view.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.FavoriteProductAdapter
import com.example.argiecommerce.databinding.FragmentWishlistBinding
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.FavoriteResponse
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.CartViewModel
import com.example.argiecommerce.viewmodel.FavoriteViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class WishlistFragment : Fragment() {
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val favoriteViewModel: FavoriteViewModel by lazy {
        ViewModelProvider(requireActivity()).get(FavoriteViewModel::class.java)
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }

    private var user: User? = null
    private lateinit var alertDialog: AlertDialog
    private lateinit var favoriteAdapter: FavoriteProductAdapter
    private val favoriteProductList: ArrayList<Product> = arrayListOf()

    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }

    private lateinit var currentProduct: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.wishlist)

        user = userViewModel.user
        setupRecyclerView()
        getFavoriteProducts()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        favoriteAdapter.onClick = {
            val action = WishlistFragmentDirections.actionWishlistFragmentToDetailsFragment(it)
            navController.navigate(action)
        }

        favoriteAdapter.onFavouriteClick = {
            currentProduct = it
            val token = loginUtils.getUserToken()
            favoriteViewModel.deleteFavoriteProduct(token, user!!.id, it.productId).observe(
                requireActivity(), { state -> processFavProductDeleting(state) }
            )
        }

        favoriteAdapter.onCartClick = {
            addToCart(it)
        }

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }
    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteProductAdapter(requireContext(), favoriteProductList)
        binding.favoriteList.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }
    }

    private fun getFavoriteProducts() {
        favoriteViewModel.getFavoriteProducts(user!!.id).observe(
            requireActivity(), { state -> processFavoriteProducts(state) }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideFavouriteList() {
        binding.favoriteList.visibility = View.GONE
        binding.emptyWishlist.visibility = View.VISIBLE
        binding.noBookmarks.visibility = View.VISIBLE
        binding.emptyBox.visibility = View.VISIBLE
    }

    private fun showFavouriteList() {
        binding.favoriteList.visibility = View.VISIBLE
        binding.emptyWishlist.visibility = View.GONE
        binding.noBookmarks.visibility = View.GONE
        binding.emptyBox.visibility = View.GONE
    }

    private fun addToCart(product: Product) {
        if(product.isNew){
            displayErrorSnackbar(getString(R.string.not_sale))
            return
        }
        if (product.isInCart == 1) {
            val token = loginUtils.getUserToken()
            cartViewModel.addToCart(token, user!!.id, product.productId).observe(
                requireActivity(), {state -> processCartResponse(state)}
            )
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
                    displayErrorSnackbar(state.message)
                }
            }
        }
    }

    private fun processFavProductDeleting(state: ScreenState<MessageResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    if(state.data.isSuccessful){
                        val pos = favoriteProductList.indexOf(currentProduct)
                        favoriteProductList.remove(currentProduct)
                        if (favoriteProductList.isEmpty()) {
                            hideFavouriteList()
                        } else {
                            showFavouriteList()
                        }
                        favoriteAdapter.notifyItemRemoved(pos)
                        Snackbar.make(requireView(), getString(R.string.delete_fav_product), Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    displayErrorSnackbar(state.message)
                }
            }
        }
    }

    private fun processFavoriteProducts(state: ScreenState<ArrayList<FavoriteResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    favoriteProductList.clear()
                    val favoriteResponseList = state.data
                    if (favoriteResponseList.isEmpty()) {
                        hideFavouriteList()
                    } else {
                        showFavouriteList()
                    }
                    for (favProduct in favoriteResponseList) {
                        val product = favProduct.product
                        product.isFavourite = 1
                        favoriteProductList.add(product)
                    }
                    favoriteAdapter.notifyDataSetChanged()
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    displayErrorSnackbar(state.message)
                }
            }
        }
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction(getString(R.string.retry_v2)) { dismiss() } }
            .show()
    }
}