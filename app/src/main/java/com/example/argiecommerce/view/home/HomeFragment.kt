package com.example.argiecommerce.view.home

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
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CategoryAdapter
import com.example.argiecommerce.adapter.FlashSaleProductAdapter
import com.example.argiecommerce.adapter.HorizontalProductAdapter
import com.example.argiecommerce.adapter.HorizontalProductLoadingAdapter
import com.example.argiecommerce.adapter.ProductLoadingAdapter
import com.example.argiecommerce.adapter.UpCommingProductAdapter
import com.example.argiecommerce.adapter.VerticalProductAdapter
import com.example.argiecommerce.databinding.FragmentHomeBinding
import com.example.argiecommerce.model.CartProduct
import com.example.argiecommerce.model.CartResponse
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.FavoriteResponse
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.model.Subcategory
import com.example.argiecommerce.model.User
import com.example.argiecommerce.network.Api
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants.CATEGORY_KEY
import com.example.argiecommerce.utils.Constants.FLASH_SALE
import com.example.argiecommerce.utils.Constants.OCOP
import com.example.argiecommerce.utils.Constants.OCOP_PRODUCT
import com.example.argiecommerce.utils.Constants.RECENT_PRODUCT
import com.example.argiecommerce.utils.Constants.SEARCH_KEY
import com.example.argiecommerce.utils.Constants.SPECIALTY
import com.example.argiecommerce.utils.Constants.SPECIALTY_PRODUCT
import com.example.argiecommerce.utils.Constants.SUBCATEGORY_KEY
import com.example.argiecommerce.utils.Constants.SUGGESTED_PRODUCT
import com.example.argiecommerce.utils.Constants.TITLE_KEY
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.NetworkMonitorUtil
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.CartViewModel
import com.example.argiecommerce.viewmodel.CategoryViewModel
import com.example.argiecommerce.viewmodel.FavoriteViewModel
import com.example.argiecommerce.viewmodel.ProductViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var flashSaleProductAdapter: FlashSaleProductAdapter
    private lateinit var ocopProductAdapter: HorizontalProductAdapter
    private lateinit var specialtyProductAdapter: HorizontalProductAdapter
    private lateinit var recentProductAdapter: UpCommingProductAdapter
    private lateinit var suggestedProductAdapter: VerticalProductAdapter

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CategoryViewModel::class.java)
    }
    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)
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
    private val apiService: Api by lazy {
        RetrofitClient.getInstance().getApi()
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }

    private lateinit var alertDialog: AlertDialog
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryItemList: ArrayList<CategoryApiResponse>
    private lateinit var networkMonitor: NetworkMonitorUtil

    private var user: User? = null
    private var productList: ArrayList<CartProduct> = arrayListOf()
    private var ocopProductId: Int = 0
    private var specialtyProductId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        user = userViewModel.user

        setupRecyclerViews()
        setupFlipImages()
        getProductData()
        cartViewModel.products = productList

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.content.tvSeeAllFlashSale.setOnClickListener(this)
        binding.content.tvSeeAllOcopProduct.setOnClickListener(this)
        binding.content.tvSeeAllSpecialtyProduct.setOnClickListener(this)
        binding.content.tvSeeAllRecentProduct.setOnClickListener(this)
        binding.content.tvSeeAllSuggestedProduct.setOnClickListener(this)
        binding.tvSearch.setOnClickListener(this)

        setupRecyclerListener()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvSearch -> goToSearchFragment()
            R.id.tvSeeAllFlashSale -> goToFlashSaleFragment()
            R.id.tvSeeAllOcopProduct -> goToStandardFragment()
            R.id.tvSeeAllSpecialtyProduct -> goToSpecialtyFragment()
            R.id.tvSeeAllRecentProduct -> goToRecentProductFragment()
            R.id.tvSeeAllSuggestedProduct -> goToSuggestedProductFragment()
        }
    }
    private fun getProductData() {
        networkMonitor.result = { isAvailable, type ->
            requireActivity().runOnUiThread {
                when (isAvailable) {
                    true -> {
                        binding.oopsLayout.visibility = View.GONE
                        binding.content.scroll.visibility = View.VISIBLE

                        lifecycleScope.launch {
                            getCategoryData()
                            getFlashSaleProduct()
                            getOcopProduct()
                            getSpecialtyProduct()
                            getRecentProduct()
                            getSuggestedProduct()
                        }
                    }

                    false -> {
                        binding.oopsLayout.visibility = View.VISIBLE
                        binding.content.scroll.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerListener() {
        categoryAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable(CATEGORY_KEY, it)
                putParcelable(SUBCATEGORY_KEY, null)
                putString(TITLE_KEY, it.categoryName)
                putString(SEARCH_KEY, null)
            }
            navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
        }

        suggestedProductAdapter.onCartClick = {
            addToCart(it)
        }
        suggestedProductAdapter.onFavouriteClick = {
            addFavoriteProduct(it)
        }
        suggestedProductAdapter.onShareClick = {
            shareProduct(it)
        }
        suggestedProductAdapter.onClick = {
            goToDetailFragment(it)
        }

        flashSaleProductAdapter.onClick = {
            goToDetailFragment(it)
        }
        flashSaleProductAdapter.onCartClick = {
            addToCart(it)
        }
        flashSaleProductAdapter.onFavouriteClick = {
            addFavoriteProduct(it)
        }
        flashSaleProductAdapter.onShareClick = {
            shareProduct(it)
        }

        ocopProductAdapter.onClick = {
            goToDetailFragment(it)
        }
        ocopProductAdapter.onCartClick = {
            addToCart(it)
        }
        ocopProductAdapter.onFavouriteClick = {
            addFavoriteProduct(it)
        }
        ocopProductAdapter.onShareClick = {
            shareProduct(it)
        }

        specialtyProductAdapter.onClick = {
            goToDetailFragment(it)
        }
        specialtyProductAdapter.onCartClick = {
            addToCart(it)
        }
        specialtyProductAdapter.onFavouriteClick = {
            addFavoriteProduct(it)
        }
        specialtyProductAdapter.onShareClick = {
            shareProduct(it)
        }

        recentProductAdapter.onClick = {
            goToDetailFragment(it)
        }
        recentProductAdapter.onCartClick = {
            addToCart(it)
        }
        recentProductAdapter.onFavouriteClick = {
            addFavoriteProduct(it)
        }
        recentProductAdapter.onShareClick = {
            shareProduct(it)
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
        Snackbar.make(requireView(), getString(R.string.share_successfully), Snackbar.LENGTH_SHORT).show()
    }
    private fun goToDetailFragment(product: Product){
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(product)
        navController.navigate(action)
    }

    private fun setupFlipImages() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.viewfilpper_1))
        imageList.add(SlideModel(R.drawable.viewfilpper_2))
        imageList.add(SlideModel(R.drawable.viewfilpper_3))
        imageList.add(SlideModel(R.drawable.viewfilpper_4))
        imageList.add(SlideModel(R.drawable.viewfilpper_5))
        imageList.add(SlideModel(R.drawable.viewfilpper_6))
        imageList.add(SlideModel(R.drawable.viewfilpper_7))
        imageList.add(SlideModel(R.drawable.viewfilpper_8))

        binding.content.imageSlider.setImageList(imageList, ScaleTypes.FIT)
    }

    private fun setupRecyclerViews() {
        // Category
        binding.content.listOfCategory.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            categoryItemList = arrayListOf()
            categoryAdapter = CategoryAdapter(requireContext(), categoryItemList)
            adapter = categoryAdapter
        }

        // Suggested Product
        binding.content.listOfSuggestedProduct.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            suggestedProductAdapter = VerticalProductAdapter(requireContext(), user)
            adapter = suggestedProductAdapter.withLoadStateHeaderAndFooter(
                header = ProductLoadingAdapter { suggestedProductAdapter.retry() },
                footer = ProductLoadingAdapter { suggestedProductAdapter.retry() }
            )
        }

        // Flash Sale
        binding.content.listOfFlashSale.apply {
            layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            flashSaleProductAdapter = FlashSaleProductAdapter(requireContext(), user)
            adapter = flashSaleProductAdapter.withLoadStateHeaderAndFooter(
                header = HorizontalProductLoadingAdapter { flashSaleProductAdapter.retry() },
                footer = HorizontalProductLoadingAdapter { flashSaleProductAdapter.retry() }
            )
        }

        // Ocop Product
        binding.content.listOfOcopProduct.apply {
            layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            ocopProductAdapter = HorizontalProductAdapter(requireContext(), user)
            adapter= ocopProductAdapter.withLoadStateHeaderAndFooter(
                header = HorizontalProductLoadingAdapter { ocopProductAdapter.retry() },
                footer = HorizontalProductLoadingAdapter { ocopProductAdapter.retry() }
            )
        }

        // Specialty Product
        binding.content.listOfSpecialtyProduct.apply {
            layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            specialtyProductAdapter = HorizontalProductAdapter(requireContext(), user)
            adapter = specialtyProductAdapter.withLoadStateHeaderAndFooter(
                header = HorizontalProductLoadingAdapter { specialtyProductAdapter.retry() },
                footer = HorizontalProductLoadingAdapter { specialtyProductAdapter.retry() }
            )
        }

        binding.content.listOfRecentProduct.apply {
            layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            recentProductAdapter = UpCommingProductAdapter(requireContext(), user)
            adapter = recentProductAdapter.withLoadStateHeaderAndFooter(
                header = HorizontalProductLoadingAdapter { recentProductAdapter.retry() },
                footer = HorizontalProductLoadingAdapter { recentProductAdapter.retry() }
            )
        }
    }

    private fun goToSuggestedProductFragment() {
        val b = Bundle().apply {
            putParcelable(CATEGORY_KEY, null)
            putParcelable(SUBCATEGORY_KEY, null)
            putString(TITLE_KEY, SUGGESTED_PRODUCT)
            putString(SEARCH_KEY, null)
        }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToSpecialtyFragment() {
        var c: CategoryApiResponse? = null
        val sb: Subcategory? = null

        for (category in categoryItemList) {
            if (category.id == specialtyProductId){
                c = category
            }
        }

        val b = Bundle().apply {
            putParcelable(CATEGORY_KEY, c)
            putParcelable(SUBCATEGORY_KEY, sb)
            putString(TITLE_KEY, SPECIALTY_PRODUCT)
            putString(SEARCH_KEY, null)
        }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToStandardFragment() {
        var c: CategoryApiResponse? = null
        var sb: Subcategory? = null

        for (category in categoryItemList) {
            for (subcategory in category.subCategoryList) {
                if (subcategory.id == ocopProductId) {
                    sb = subcategory
                    c = category
                }
            }
        }

        val b = Bundle().apply {
            putParcelable(CATEGORY_KEY, c)
            putParcelable(SUBCATEGORY_KEY, sb)
            putString(TITLE_KEY, OCOP_PRODUCT)
            putString(SEARCH_KEY, null)
        }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToFlashSaleFragment() {
        val b = Bundle().apply {
            putParcelable(CATEGORY_KEY, null)
            putParcelable(SUBCATEGORY_KEY, null)
            putString(TITLE_KEY, FLASH_SALE)
            putString(SEARCH_KEY, null)
        }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToRecentProductFragment() {
        val b = Bundle().apply {
            putParcelable(CATEGORY_KEY, null)
            putParcelable(SUBCATEGORY_KEY, null)
            putString(TITLE_KEY, RECENT_PRODUCT)
            putString(SEARCH_KEY, null)
        }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToSearchFragment() {
        navController.navigate(R.id.action_homeFragment_to_searchFragment)
    }

    suspend fun getCategoryData() {
        withContext(Dispatchers.IO){
            val response = apiService.getCategories()
            if (response.isSuccessful){
                if (response.body() != null){
                    val categoryList = response.body()
                    if (!categoryList!!.isEmpty()){
                        categoryItemList.clear()
                        categoryItemList.addAll(categoryList)
                        for (category in categoryItemList){
                            if (category.categoryName.equals(SPECIALTY)){
                                specialtyProductId = category.id
                            }
                            for (subcategory in category.subCategoryList){
                                if (subcategory.subcategoryName.equals(OCOP)){
                                    ocopProductId = subcategory.id
                                }
                            }
                        }
                        withContext(Dispatchers.Main){
                            categoryAdapter.notifyDataSetChanged()
                        }
                    }

                }
            }
        }
    }

    private fun getSuggestedProduct() {
        val productApiRequest = ProductApiRequest()
        lifecycleScope.launch {
            productViewModel.getAllProducts(productApiRequest).collectLatest { pagingData ->
                suggestedProductAdapter.submitData(pagingData)
            }
        }
    }

    private fun getSpecialtyProduct() {
        val productApiRequest = ProductApiRequest(specialtyProductId.toLong())
        lifecycleScope.launch {
            productViewModel.getProducts(productApiRequest).collectLatest { pagingData ->
                    specialtyProductAdapter.submitData(pagingData)
            }
        }
    }

    private fun getOcopProduct() {
        val productApiRequest = ProductApiRequest(ocopProductId.toLong())
        lifecycleScope.launch {
            productViewModel.getProductBySubCategory(productApiRequest).collectLatest { pagingData ->
                    ocopProductAdapter.submitData(pagingData)
            }
        }
    }

    private fun getRecentProduct() {
        val productApiRequest = ProductApiRequest()
        lifecycleScope.launch {
            productViewModel.getUpcomingProducts(productApiRequest).collectLatest { pagingData ->
                recentProductAdapter.submitData(pagingData)
            }
        }
    }

    private fun getFlashSaleProduct() {
        val productApiRequest = ProductApiRequest()
        lifecycleScope.launch {
            productViewModel.getProductsWithDiscount(productApiRequest)
                .collectLatest { pagingData ->
                    flashSaleProductAdapter.submitData(pagingData)
                }
        }
    }

    private fun processCategoryResponse(state: ScreenState<ArrayList<CategoryApiResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    categoryItemList.clear()
                    categoryItemList.addAll(state.data)
                    categoryAdapter.notifyDataSetChanged()
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
                    displayErrorSnackbar(state.message)
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

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.register()
    }

    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkMonitor = NetworkMonitorUtil(requireContext())
    }
}