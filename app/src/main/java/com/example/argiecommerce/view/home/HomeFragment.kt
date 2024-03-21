package com.example.argiecommerce.view.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CategoryAdapter
import com.example.argiecommerce.adapter.VerticalProductAdapter
import com.example.argiecommerce.adapter.HorizontalProductAdapter
import com.example.argiecommerce.databinding.FragmentHomeBinding
import com.example.argiecommerce.model.CategoryApiResponse
import com.example.argiecommerce.model.CategoryItem
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.utils.ImageUtils
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils
import com.example.argiecommerce.viewmodel.CategoryViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), View.OnClickListener,
    VerticalProductAdapter.DemoAdapterOnClickListener,
    HorizontalProductAdapter.DemoAdapterOnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var categoryItemList: ArrayList<CategoryItem>
    private lateinit var flashSaleProductList: ArrayList<Product>
    private lateinit var ocopProductList: ArrayList<Product>
    private lateinit var specialtyProductList: ArrayList<Product>
    private lateinit var recentProductList: ArrayList<Product>
    private lateinit var suggestedProductList: ArrayList<Product>

    private lateinit var flashSaleProductAdapter: HorizontalProductAdapter
    private lateinit var ocopProductAdapter: HorizontalProductAdapter
    private lateinit var specialtyProductAdapter: HorizontalProductAdapter
    private lateinit var recentProductAdapter: HorizontalProductAdapter
    private lateinit var suggestedProductAdapter: VerticalProductAdapter

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CategoryViewModel::class.java)
    }
    private lateinit var alertDialog: AlertDialog

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setUpViews()
        getCategoryData()
        getSuggestedProduct()
        getFlashSaleProduct()
        getOcopProduct()
        getSpecialtyProduct()
        getRecentProduct()

        setFlipImages()

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
        binding.content.listOfCategory.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfCategory.setHasFixedSize(true)
        categoryItemList = arrayListOf()

        binding.content.listOfSuggestedProduct.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.content.listOfSuggestedProduct.setHasFixedSize(true)
        suggestedProductList = arrayListOf()

        binding.content.listOfFlashSale.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfFlashSale.setHasFixedSize(true)
        flashSaleProductList = arrayListOf()

        binding.content.listOfOcopProduct.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfOcopProduct.setHasFixedSize(true)
        ocopProductList = arrayListOf()

        binding.content.listOfSpecialtyProduct.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfSpecialtyProduct.setHasFixedSize(true)
        specialtyProductList = arrayListOf()

        binding.content.listOfRecentProduct.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.content.listOfRecentProduct.setHasFixedSize(true)
        recentProductList = arrayListOf()
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
        val b = Bundle().apply { putString("category", "Gợi ý hôm nay") }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToRecentProductFragment() {
        val b = Bundle().apply { putString("category", "Sản phẩm gần đây") }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToSpecialtyFragment() {
        val b = Bundle().apply { putString("category", "Đặc sản vùng miền") }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToStandardFragment() {
        val b = Bundle().apply { putString("category", "Sản phẩm đạt chuẩn") }
        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
    }

    private fun goToFlashSaleFragment() {
        val b = Bundle().apply { putString("category", "Flash Sale") }
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
        val demo0 = Product("Apple", 50000.00, 50, "HTX HN", "Vegetable")
        val demo1 = Product("Orange", 150000.00, 50, "HTX HN", "Vegetable")
        val demo2 = Product("Strawberry", 1050000.00, 50, "HTX HN", "Vegetable")
        val demo3 = Product("Coconut", 9550000.00, 50, "HTX HN", "Vegetable")

        demo0.productImage = R.drawable.product_demo.toString()
        demo1.productImage = R.drawable.product_demo.toString()
        demo2.productImage = R.drawable.product_demo.toString()
        demo3.productImage = R.drawable.product_demo.toString()
        suggestedProductList.add(demo0)
        suggestedProductList.add(demo1)
        suggestedProductList.add(demo2)
        suggestedProductList.add(demo3)


        suggestedProductAdapter = VerticalProductAdapter(suggestedProductList, this)
        binding.content.listOfSuggestedProduct.adapter = suggestedProductAdapter

    }

    private fun getRecentProduct() {
        recentProductAdapter = HorizontalProductAdapter(suggestedProductList, this)
        binding.content.listOfRecentProduct.adapter = recentProductAdapter
    }

    private fun getSpecialtyProduct() {
        specialtyProductAdapter = HorizontalProductAdapter(suggestedProductList, this)
        binding.content.listOfSpecialtyProduct.adapter = specialtyProductAdapter
    }

    private fun getOcopProduct() {
        ocopProductAdapter = HorizontalProductAdapter(suggestedProductList, this)
        binding.content.listOfOcopProduct.adapter = ocopProductAdapter
    }

    private fun getFlashSaleProduct() {
        flashSaleProductAdapter = HorizontalProductAdapter(suggestedProductList, this)
        binding.content.listOfFlashSale.adapter = flashSaleProductAdapter
    }

    override fun onClick(product: Product) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(product)
        navController.navigate(action)
    }

    private fun processCategoryResponse(state: ScreenState<List<CategoryApiResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    categoryAdapter = CategoryAdapter(requireContext(), state.data)
                    categoryAdapter.onClick = {
                        val b = Bundle().apply { putString("category", it.categoryName) }
                        navController.navigate(R.id.action_homeFragment_to_seeAllFragment, b)
                    }
                    binding.content.listOfCategory.adapter = categoryAdapter
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
}