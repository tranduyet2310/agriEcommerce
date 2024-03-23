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
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CategoryAdapter
import com.example.argiecommerce.adapter.FlashSaleProductAdapter
import com.example.argiecommerce.adapter.HorizontalProductAdapter
import com.example.argiecommerce.adapter.UpCommingProductAdapter
import com.example.argiecommerce.adapter.VerticalProductAdapter
import com.example.argiecommerce.databinding.FragmentHomeBinding
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.model.ProductApiRequest
import com.example.argiecommerce.model.ProductApiResponse
import com.example.argiecommerce.utils.Constants.CATEGORY_KEY
import com.example.argiecommerce.utils.Constants.FLASH_SALE
import com.example.argiecommerce.utils.Constants.OCOP_PRODUCT
import com.example.argiecommerce.utils.Constants.RECENT_PRODUCT
import com.example.argiecommerce.utils.Constants.SPECIALTY_PRODUCT
import com.example.argiecommerce.utils.Constants.SUGGESTED_PRODUCT
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.CategoryViewModel
import com.example.argiecommerce.viewmodel.ProductViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var categoryItemList: ArrayList<CategoryApiResponse>
    private lateinit var flashSaleProductList: ArrayList<Product>
    private lateinit var ocopProductList: ArrayList<Product>
    private lateinit var specialtyProductList: ArrayList<Product>
    private lateinit var recentProductList: ArrayList<Product>
    private lateinit var suggestedProductList: ArrayList<Product>

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

    private lateinit var alertDialog: AlertDialog
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setUpViews()
        setFlipImages()

        getCategoryData()
        getFlashSaleProduct()
        getOcopProduct()
        getSpecialtyProduct()
        getRecentProduct()
        getSuggestedProduct()

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
    }

    private fun setFlipImages() {
        val imageList = ArrayList<SlideModel>();
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

    private fun setUpViews() {
        // Category
        binding.content.listOfCategory.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfCategory.setHasFixedSize(true)
        categoryItemList = arrayListOf()
        categoryAdapter = CategoryAdapter(requireContext(), categoryItemList)
        categoryAdapter.onClick = {
            val b = Bundle().apply { putString(CATEGORY_KEY, it.categoryName) }
            navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
        }
        binding.content.listOfCategory.adapter = categoryAdapter

        // Suggested Product
        binding.content.listOfSuggestedProduct.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.content.listOfSuggestedProduct.setHasFixedSize(true)
        suggestedProductList = arrayListOf()
        suggestedProductAdapter = VerticalProductAdapter(requireContext(), suggestedProductList)
        suggestedProductAdapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            navController.navigate(action)
        }
        binding.content.listOfSuggestedProduct.adapter = suggestedProductAdapter

        // Flash Sale
        binding.content.listOfFlashSale.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfFlashSale.setHasFixedSize(true)
        flashSaleProductList = arrayListOf()
        flashSaleProductAdapter = FlashSaleProductAdapter(requireContext(), flashSaleProductList)
        flashSaleProductAdapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            navController.navigate(action)
        }
        binding.content.listOfFlashSale.adapter = flashSaleProductAdapter

        // Ocop Product
        binding.content.listOfOcopProduct.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfOcopProduct.setHasFixedSize(true)
        ocopProductList = arrayListOf()
        ocopProductAdapter = HorizontalProductAdapter(requireContext(), ocopProductList)
        ocopProductAdapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            navController.navigate(action)
        }
        binding.content.listOfOcopProduct.adapter = ocopProductAdapter

        // Specialty Product
        binding.content.listOfSpecialtyProduct.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfSpecialtyProduct.setHasFixedSize(true)
        specialtyProductList = arrayListOf()
        specialtyProductAdapter = HorizontalProductAdapter(requireContext(), specialtyProductList)
        specialtyProductAdapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            navController.navigate(action)
        }
        binding.content.listOfSpecialtyProduct.adapter = specialtyProductAdapter

        // Recent Product
        binding.content.listOfRecentProduct.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfRecentProduct.setHasFixedSize(true)
        recentProductList = arrayListOf()
        recentProductAdapter = UpCommingProductAdapter(requireContext(), recentProductList)
        recentProductAdapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            navController.navigate(action)
        }
        binding.content.listOfRecentProduct.adapter = recentProductAdapter
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
        val b = Bundle().apply { putString(CATEGORY_KEY, SUGGESTED_PRODUCT) }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToRecentProductFragment() {
        val b = Bundle().apply { putString(CATEGORY_KEY, RECENT_PRODUCT) }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToSpecialtyFragment() {
        val b = Bundle().apply { putString(CATEGORY_KEY, SPECIALTY_PRODUCT) }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToStandardFragment() {
        val b = Bundle().apply { putString(CATEGORY_KEY, OCOP_PRODUCT) }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToFlashSaleFragment() {
        val b = Bundle().apply { putString(CATEGORY_KEY, FLASH_SALE) }
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
        productViewModel.getProductByCategoryId(productApiRequest)
            .observe(
                requireActivity(),
                { state -> processProductResponse(state, productApiRequest.id) })
    }

    private fun getRecentProduct() {
        val productApiRequest = ProductApiRequest(5)
        productViewModel.getProductByCategoryId(productApiRequest)
            .observe(
                requireActivity(),
                { state -> processProductResponse(state, productApiRequest.id) })
    }

    private fun getSpecialtyProduct() {
        val productApiRequest = ProductApiRequest(4)
        productViewModel.getProductByCategoryId(productApiRequest)
            .observe(
                requireActivity(),
                { state -> processProductResponse(state, productApiRequest.id) })
    }

    private fun getOcopProduct() {
        val productApiRequest = ProductApiRequest(3)
        productViewModel.getProductByCategoryId(productApiRequest)
            .observe(
                requireActivity(),
                { state -> processProductResponse(state, productApiRequest.id) })
    }

    private fun getFlashSaleProduct() {
        val productApiRequest = ProductApiRequest(1)
        productViewModel.getProductByCategoryId(productApiRequest)
            .observe(
                requireActivity(),
                { state -> processProductResponse(state, productApiRequest.id) })
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

    private fun processProductResponse(state: ScreenState<ProductApiResponse?>, id: Long) {
        when (state) {
            is ScreenState.Loading -> {
                binding.appendProgress.visibility = View.VISIBLE
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    binding.appendProgress.visibility = View.GONE
                    when (id) {
                        1L -> setupFlashSaleAdapter(state.data.content)
                        2L -> setupSuggestedAdapter(state.data.content)
                        3L -> setupOcopAdapter(state.data.content)
                        4L -> setupSpecialtyAdapter(state.data.content)
                        5L -> setupRecentAdapter(state.data.content)
                    }
                }
            }

            is ScreenState.Error -> {
                binding.appendProgress.visibility = View.GONE
                if (state.message != null) {
                    displayErrorSnackbar(state.message)
                }
            }
        }
    }

    private fun setupFlashSaleAdapter(content: ArrayList<Product>) {
        flashSaleProductList.addAll(content)
        flashSaleProductAdapter.notifyDataSetChanged()
    }

    private fun setupSuggestedAdapter(data: ArrayList<Product>) {
        suggestedProductList.addAll(data)
        suggestedProductAdapter.notifyDataSetChanged()
    }

    private fun setupOcopAdapter(data: ArrayList<Product>) {
        ocopProductList.addAll(data)
        ocopProductAdapter.notifyDataSetChanged()
    }

    private fun setupSpecialtyAdapter(data: ArrayList<Product>) {
        specialtyProductList.addAll(data)
        specialtyProductAdapter.notifyDataSetChanged()
    }

    private fun setupRecentAdapter(data: ArrayList<Product>) {
        recentProductList.addAll(data)
        recentProductAdapter.notifyDataSetChanged()
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction("Thử lại 👍") { dismiss() } }
            .show()
    }
}