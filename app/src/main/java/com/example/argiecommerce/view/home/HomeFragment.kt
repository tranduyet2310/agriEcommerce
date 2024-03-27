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
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.model.Subcategory
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants.CATEGORY_KEY
import com.example.argiecommerce.utils.Constants.FLASH_SALE
import com.example.argiecommerce.utils.Constants.OCOP_PRODUCT
import com.example.argiecommerce.utils.Constants.RECENT_PRODUCT
import com.example.argiecommerce.utils.Constants.SEARCH_KEY
import com.example.argiecommerce.utils.Constants.SPECIALTY_PRODUCT
import com.example.argiecommerce.utils.Constants.SUBCATEGORY_KEY
import com.example.argiecommerce.utils.Constants.SUGGESTED_PRODUCT
import com.example.argiecommerce.utils.Constants.TITLE_KEY
import com.example.argiecommerce.utils.NetworkMonitorUtil
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.CategoryViewModel
import com.example.argiecommerce.viewmodel.ProductViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

    private lateinit var alertDialog: AlertDialog
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryItemList: ArrayList<CategoryApiResponse>

    private lateinit var networkMonitor: NetworkMonitorUtil
    private var user: User? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        user = userViewModel.user

        setupRecyclerViews()
        setupFlipImages()
        getProductData()

        return binding.root
    }

    private fun getProductData() {
        networkMonitor.result = { isAvailable, type ->
            requireActivity().runOnUiThread {
                when (isAvailable) {
                    true -> {
                        binding.oopsLayout.visibility = View.GONE
                        binding.content.scroll.visibility = View.VISIBLE

                        getCategoryData()
                        getFlashSaleProduct()
                        getOcopProduct()
                        getSpecialtyProduct()
                        getRecentProduct()
                        getSuggestedProduct()
                    }

                    false -> {
                        binding.oopsLayout.visibility = View.VISIBLE
                        binding.content.scroll.visibility = View.INVISIBLE
                    }
                }
            }
        }
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
        binding.content.listOfCategory.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfCategory.setHasFixedSize(true)
        categoryItemList = arrayListOf()
        categoryAdapter = CategoryAdapter(requireContext(), categoryItemList)
        categoryAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable(CATEGORY_KEY, it)
                putParcelable(SUBCATEGORY_KEY, null)
                putString(TITLE_KEY, it.categoryName)
                putString(SEARCH_KEY, null)
            }
            navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
        }
        binding.content.listOfCategory.adapter = categoryAdapter

        // Suggested Product
        binding.content.listOfSuggestedProduct.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.content.listOfSuggestedProduct.setHasFixedSize(true)
        suggestedProductAdapter = VerticalProductAdapter(requireContext(), user)
        suggestedProductAdapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            navController.navigate(action)
        }
        binding.content.listOfSuggestedProduct.adapter =
            suggestedProductAdapter.withLoadStateHeaderAndFooter(
                header = ProductLoadingAdapter { suggestedProductAdapter.retry() },
                footer = ProductLoadingAdapter { suggestedProductAdapter.retry() }
            )

        // Flash Sale
        binding.content.listOfFlashSale.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfFlashSale.setHasFixedSize(true)
        flashSaleProductAdapter = FlashSaleProductAdapter(requireContext(), user)
        flashSaleProductAdapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            navController.navigate(action)
        }
        binding.content.listOfFlashSale.adapter =
            flashSaleProductAdapter.withLoadStateHeaderAndFooter(
                header = HorizontalProductLoadingAdapter { flashSaleProductAdapter.retry() },
                footer = HorizontalProductLoadingAdapter { flashSaleProductAdapter.retry() }
            )

        // Ocop Product
        binding.content.listOfOcopProduct.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfOcopProduct.setHasFixedSize(true)
        ocopProductAdapter = HorizontalProductAdapter(requireContext(), user)
        ocopProductAdapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            navController.navigate(action)
        }
        binding.content.listOfOcopProduct.adapter = ocopProductAdapter.withLoadStateHeaderAndFooter(
            header = HorizontalProductLoadingAdapter { ocopProductAdapter.retry() },
            footer = HorizontalProductLoadingAdapter { ocopProductAdapter.retry() }
        )

        // Specialty Product
        binding.content.listOfSpecialtyProduct.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfSpecialtyProduct.setHasFixedSize(true)
        specialtyProductAdapter = HorizontalProductAdapter(requireContext(), user)
        specialtyProductAdapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            navController.navigate(action)
        }
        binding.content.listOfSpecialtyProduct.adapter =
            specialtyProductAdapter.withLoadStateHeaderAndFooter(
                header = HorizontalProductLoadingAdapter { specialtyProductAdapter.retry() },
                footer = HorizontalProductLoadingAdapter { specialtyProductAdapter.retry() }
            )

        // Recent Product
        binding.content.listOfRecentProduct.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfRecentProduct.setHasFixedSize(true)
        recentProductAdapter = UpCommingProductAdapter(requireContext(), user)
        recentProductAdapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            navController.navigate(action)
        }
        binding.content.listOfRecentProduct.adapter =
            recentProductAdapter.withLoadStateHeaderAndFooter(
                header = HorizontalProductLoadingAdapter { recentProductAdapter.retry() },
                footer = HorizontalProductLoadingAdapter { recentProductAdapter.retry() }
            )
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

    private fun goToSuggestedProductFragment() {
        var c: CategoryApiResponse? = null
        var sb: Subcategory? = null

        for (category in categoryItemList) {
            for (subcategory in category.subCategoryList) {
                if (subcategory.subcategoryName.equals("Táo")) {
                    sb = subcategory
                    c = category
                }
            }
        }

        val b = Bundle().apply {
            putParcelable(CATEGORY_KEY, c)
            putParcelable(SUBCATEGORY_KEY, sb)
            putString(TITLE_KEY, SUGGESTED_PRODUCT)
            putString(SEARCH_KEY, null)
        }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToSpecialtyFragment() {
        var c: CategoryApiResponse? = null
        var sb: Subcategory? = null

        for (category in categoryItemList) {
            for (subcategory in category.subCategoryList) {
                if (subcategory.subcategoryName.equals("Táo khô")) {
                    sb = subcategory
                    c = category
                }
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
                if (subcategory.subcategoryName.equals("Rau cải thìa")) {
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

    private fun getCategoryData() {
        categoryViewModel.getCategoryResponseData()
            .observe(requireActivity(), { state -> processCategoryResponse(state) })
    }

    private fun getSuggestedProduct() {
        val productApiRequest = ProductApiRequest(2)
        lifecycleScope.launch {
            productViewModel.getProducts(productApiRequest).collectLatest { pagingData ->
                suggestedProductAdapter.submitData(pagingData)
            }
        }
    }


    private fun getSpecialtyProduct() {
        val productApiRequest = ProductApiRequest(9)
        lifecycleScope.launch {
            productViewModel.getProductBySubCategory(productApiRequest)
                .collectLatest { pagingData ->
                    specialtyProductAdapter.submitData(pagingData)
                }
        }
    }

    private fun getOcopProduct() {
        val productApiRequest = ProductApiRequest(2)
        lifecycleScope.launch {
            productViewModel.getProductBySubCategory(productApiRequest)
                .collectLatest { pagingData ->
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
                val progressDialog = ProgressDialog()
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

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction("Thử lại 👍") { dismiss() } }
            .show()
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