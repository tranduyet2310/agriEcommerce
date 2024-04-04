package com.example.argiecommerce.view.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CartProductAdapter
import com.example.argiecommerce.databinding.FragmentCartBinding
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils.Companion.formatPrice
import com.example.argiecommerce.viewmodel.CartViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class CartFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }

    private lateinit var alertDialog: AlertDialog
    private var user: User? = null
    private var totalPrice = 0L
    private lateinit var cartAdapter: CartProductAdapter
    private var cartItemList: ArrayList<CartResponse> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.cart)

        user = userViewModel.user
        setupRecyclerView()
        getCartItems()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.buttonCheckout.setOnClickListener(this)
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        cartAdapter.onProductClick = {
            val b = Bundle().apply {
                putParcelable(Constants.PRODUCT_KEY, it.product)
            }
            navController.navigate(R.id.action_cartFragment_to_detailsFragment, b)
        }

        cartAdapter.onPlusClick = {
            val newQuantity = it.quantity + 1
            val position = cartItemList.indexOf(it)
            val cartItem = cartItemList[position]
            cartItem.quantity = newQuantity

            val token = loginUtils.getUserToken()
            cartViewModel.changeQuantity(token, user!!.id, it.product.productId, newQuantity)
                .observe(
                    requireActivity(), { state -> processChangeQuantity(state, position) }
                )
        }

        cartAdapter.onMinusClick = {
            val newQuantity = it.quantity - 1
            val position = cartItemList.indexOf(it)
            val cartItem = cartItemList[position]

            if (newQuantity <= 0) {
                cartItem.quantity = 0
                showConfirmDialog(it.product.productId, position)
            } else {
                cartItem.quantity = newQuantity
                val token = loginUtils.getUserToken()
                cartViewModel.changeQuantity(token, user!!.id, it.product.productId, newQuantity)
                    .observe(
                        requireActivity(), { state -> processChangeQuantity(state, position) }
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonCheckout -> goToBillingFragment()
        }
    }

    private fun getCartItems() {
        cartViewModel.getAllCartItems(user!!.id).observe(
            requireActivity(), { state -> processCartResponse(state) }
        )
    }

    private fun setupRecyclerView() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            cartAdapter = CartProductAdapter(cartItemList)
            adapter = cartAdapter
        }
    }

    private fun goToBillingFragment() {
        navController.navigate(R.id.action_cartFragment_to_billingFragment)
    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun hideEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.GONE
        }
    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
        }
    }

    private fun processCartResponse(state: ScreenState<ArrayList<CartResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    if (state.data.isEmpty()) {
                        showEmptyCart()
                        hideOtherViews()
                    } else {
                        hideEmptyCart()
                        showOtherViews()
                        cartItemList.clear()
                        cartItemList.addAll(state.data)
                        cartAdapter.notifyDataSetChanged()
                        totalPrice = 0L
                        calculateTotalPrice(state.data)
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

    private fun processChangeQuantity(state: ScreenState<CartResponse?>, position: Int) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    if (cartItemList.isEmpty()) {
                        showEmptyCart()
                        hideOtherViews()
                    } else {
                        hideEmptyCart()
                        showOtherViews()
//                        cartAdapter.notifyItemChanged(position)
                        cartAdapter.notifyDataSetChanged()
                        totalPrice = 0L
                        calculateTotalPrice(cartItemList)
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

    private fun handleRemoveFromCart(state: ScreenState<MessageResponse?>, position: Int) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    cartItemList.removeAt(position)
//                    cartAdapter.notifyItemChanged(position)
                    cartAdapter.notifyDataSetChanged()
                    totalPrice = 0L
                    calculateTotalPrice(cartItemList)
                    if (cartItemList.isEmpty()) {
                        showEmptyCart()
                        hideOtherViews()
                    } else {
                        hideEmptyCart()
                        showOtherViews()
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

    private fun showConfirmDialog(productId: Long, position: Int) {
        val dialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.check_out))
            setMessage(getString(R.string.delete_from_cart))
            setNegativeButton(getString(R.string.cancel_v2)) { dialogs, _ ->
                dialogs.dismiss()
            }
            setPositiveButton(getString(R.string.ok)) { dialogs, _ ->
                val token = loginUtils.getUserToken()
                cartViewModel.removeFromCart(token, user!!.id, productId).observe(
                    requireActivity(), { state -> handleRemoveFromCart(state, position) }
                )
                dialogs.dismiss()
            }
        }
        dialog.create()
        dialog.show()
    }

    private fun calculateTotalPrice(cartResponse: ArrayList<CartResponse>) {
        for (item in cartResponse) {
            totalPrice += if (item.product.discountPrice > 0) {
                item.product.discountPrice * item.quantity
            } else {
                item.product.standardPrice * item.quantity
            }
        }
        binding.tvTotalPrice.text = totalPrice.formatPrice()
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction(Constants.RETRY) { dismiss() } }
            .show()
    }
}