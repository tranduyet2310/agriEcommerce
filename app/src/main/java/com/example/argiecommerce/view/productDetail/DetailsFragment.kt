package com.example.argiecommerce.view.productDetail

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentDetailsBinding
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.Image
import com.example.argiecommerce.model.MessageResponse
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ReviewStatisticResponse
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants.PRODUCT_ID_KEY
import com.example.argiecommerce.utils.Constants.SUPPLIER_KEY
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils.Companion.formatPrice
import com.example.argiecommerce.viewmodel.CartViewModel
import com.example.argiecommerce.viewmodel.OrderViewModel
import com.example.argiecommerce.viewmodel.ReviewViewModel
import com.example.argiecommerce.viewmodel.SupplierViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class DetailsFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val args: DetailsFragmentArgs by navArgs()

    private var isExpanded: Boolean = false
    private lateinit var product: Product

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider(requireActivity()).get(OrderViewModel::class.java)
    }
    private val reviewViewModel: ReviewViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ReviewViewModel::class.java)
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }
    private val supplierViewModel: SupplierViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SupplierViewModel::class.java)
    }

    private var user: User? = null
    private lateinit var alertDialog: AlertDialog
    private var hasPurchased: Boolean = false
    private var rating: Double = 0.0
    private var supplierImageUrl: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.infor_product)

        product = args.productValue
        user = userViewModel.user

        getAverageRating()
        setupProductImage(product)
        setupProductInfo(product)
        setupSupplierInfo(product)
        if (user != null) checkPurchase()
        getSupplierBasicInfo()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.buy.setOnClickListener(this)
        binding.addToCart.setOnClickListener(this)
        binding.details.writeReview.setOnClickListener(this)
        binding.details.tvSeeAllReviews.setOnClickListener(this)
        binding.details.btnSeeMoreInfoProduct.setOnClickListener(this)
        binding.details.btnSeeShop.setOnClickListener(this)
        binding.details.imgFavourite.setOnClickListener(this)
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buy -> buyProduct()
            R.id.addToCart -> addProductToCart()
            R.id.writeReview -> writeReview()
            R.id.tvSeeAllReviews -> seeAllReviews()
            R.id.btnSeeMoreInfoProduct -> seeMoreInfoProduct()
            R.id.btnSeeShop -> goToSuppilerShop()
            R.id.imgFavourite -> addFavouriteProduct()
        }
    }

    private fun addFavouriteProduct() {
        if (product.isFavourite != 1) {
            binding.details.imgFavourite.setImageResource(R.drawable.ic_favorite_red)
            product.isFavourite = 1
        } else {
            binding.details.imgFavourite.setImageResource(R.drawable.ic_favorite_border)
            product.isFavourite = 0
        }
    }

    private fun goToSuppilerShop() {
        val supplierBasicInfo = SupplierBasicInfo()
        supplierBasicInfo.supplierId = product.supplierId
        supplierBasicInfo.supplierShopName = product.productSupplier
        supplierBasicInfo.imageUrl = supplierImageUrl
        supplierBasicInfo.supplierProvince = product.supplierProvince
        supplierBasicInfo.rating = rating
        val b = Bundle().apply {
            putParcelable(SUPPLIER_KEY, supplierBasicInfo)
        }
        userViewModel.supplierBasicInfo = supplierBasicInfo
        navController.navigate(R.id.action_detailsFragment_to_suppilerFragment, b)
    }

    private fun seeMoreInfoProduct() {
        if (isExpanded) {
            binding.details.tvInfoDetailsOfProduct.maxLines = 5
            binding.details.tvInfoDetailsOfProduct.ellipsize = TextUtils.TruncateAt.END
            binding.details.btnSeeMoreInfoProduct.text = buildString {
                append(requireContext().resources.getString(R.string.see_more))
            }
            isExpanded = false
        } else {
            binding.details.tvInfoDetailsOfProduct.maxLines = Int.MAX_VALUE
            binding.details.tvInfoDetailsOfProduct.ellipsize = null
            binding.details.btnSeeMoreInfoProduct.text = buildString {
                append(requireContext().resources.getString(R.string.see_less))
            }
            isExpanded = true
        }
    }

    private fun seeAllReviews() {
        val b = Bundle().apply {
            putLong(PRODUCT_ID_KEY, product.productId)
        }
        navController.navigate(R.id.action_detailsFragment_to_allReviewsFragment, b)
    }

    private fun writeReview() {
        if (user == null) {
            val dialog = ProgressDialog.createMessageDialog(
                requireContext(),
                requireContext().resources.getString(R.string.need_to_login)
            )
            dialog.show()
        } else {
            if (hasPurchased) {
                val b = Bundle().apply {
                    putLong(PRODUCT_ID_KEY, product.productId)
                }
                navController.navigate(R.id.action_detailsFragment_to_writeReviewFragment, b)
            } else {
                showSnackbar(getString(R.string.allow_review))
            }
        }
    }

    private fun addProductToCart() {
        if (user == null) {
            val dialog = ProgressDialog.createMessageDialog(
                requireContext(),
                requireContext().resources.getString(R.string.need_to_login)
            )
            dialog.show()
        } else {
            val token = loginUtils.getUserToken()
            cartViewModel.addToCart(token, user!!.id, product.productId).observe(
                requireActivity(), { state -> processCartResponse(state, false) }
            )
        }
    }

    private fun buyProduct() {
        if (user == null) {
            val dialog = ProgressDialog.createMessageDialog(
                requireContext(),
                requireContext().resources.getString(R.string.need_to_login)
            )
            dialog.show()
        } else {
            val token = loginUtils.getUserToken()
            cartViewModel.addToCart(token, user!!.id, product.productId).observe(
                requireActivity(), { state -> processCartResponse(state, true) }
            )

        }

    }

    private fun checkPurchase() {
        orderViewModel.checkUserPurchasedOrNot(user!!.id, product.productId).observe(
            requireActivity(), { state -> processCheckPurchased(state) }
        )
    }

    private fun getAverageRating() {
        reviewViewModel.averageRating(product.productId).observe(
            requireActivity(), { state -> processAverageRating(state) }
        )
    }

    private fun getSupplierBasicInfo() {
        supplierViewModel.getSupplierAvatar(product.supplierId).observe(
            requireActivity(), {state -> processGetSupplierAvatar(state)}
        )
    }

    private fun setupSupplierInfo(product: Product) {
        binding.details.tvProvinceOfProvider.text = product.supplierProvince
        binding.details.tvNameOfProvider.text = product.productSupplier
    }

    private fun setupProductInfo(product: Product) {
        if (product.discountPrice > 0) {
            binding.details.priceOfProduct.text = product.discountPrice.formatPrice()
            binding.details.priceOfProductDiscount.text = product.standardPrice.formatPrice()
        } else {
            binding.details.priceOfProduct.text = product.standardPrice.formatPrice()
            binding.details.priceOfProductDiscount.visibility = View.GONE
            binding.details.divider.visibility = View.GONE
        }

        binding.details.tvRatingMiniView.text = "4.5"

        binding.details.nameOfProduct.text = product.productName
        binding.details.tvInfoDetailsOfProduct.text = product.description
        binding.details.tvProductSold.text = product.sold.toString()

        if (product.isFavourite == 1) {
            binding.details.imgFavourite.setImageResource(R.drawable.ic_favorite_red)
        } else {
            binding.details.imgFavourite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    private fun setupProductImage(product: Product) {
        val imageList = ArrayList<SlideModel>()
        for (image in product.productImage) {
            imageList.add(SlideModel(image.imageUrl))
        }
        binding.details.imageOfProduct.setImageList(imageList, ScaleTypes.FIT)
    }


    private fun processCheckPurchased(state: ScreenState<MessageResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    hasPurchased = state.data.isSuccessful
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

    private fun processAverageRating(state: ScreenState<ReviewStatisticResponse?>) {
        when (state) {
            is ScreenState.Loading -> {}

            is ScreenState.Success -> {
                if (state.data != null) {
//                    alertDialog.dismiss()
                    rating = state.data.averageRating.toDouble()
                    if (rating == 0.0) {
                        binding.details.tvRatingMiniView.text = getString(R.string._5_0)
                    } else {
                        binding.details.tvRatingMiniView.text = rating.toString()
                    }

                }
            }

            is ScreenState.Error -> {
//                alertDialog.dismiss()
                if (state.message != null) {
                    showSnackbar(state.message)
                }
            }
        }
    }

    private fun processGetSupplierAvatar(state: ScreenState<Image?>) {
        when (state) {
            is ScreenState.Loading -> {}

            is ScreenState.Success -> {
                if (state.data != null) {
//                    alertDialog.dismiss()
                    supplierImageUrl = state.data.imageUrl
                    if (state.data.imageUrl != null){
                        GlideApp.with(requireContext())
                            .load(state.data.imageUrl)
                            .into(binding.details.imageOfProvider)
                    }
                }
            }

            is ScreenState.Error -> {
//                alertDialog.dismiss()
                if (state.message != null) {
                    showSnackbar(state.message)
                }
            }
        }
    }

    private fun processCartResponse(state: ScreenState<CartResponse?>, isBuyNow: Boolean) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    if (isBuyNow) {
                        navController.navigate(R.id.action_detailsFragment_to_billingFragment)
                    } else {
                        showSnackbar(getString(R.string.add_into_cart))
                    }
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