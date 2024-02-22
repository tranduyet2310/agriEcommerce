package com.example.argiecommerce.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CategoryAdapter
import com.example.argiecommerce.adapter.DemoAdapter
import com.example.argiecommerce.adapter.DemoAdapter2
import com.example.argiecommerce.databinding.FragmentHomeBinding
import com.example.argiecommerce.model.CategoryItem
import com.example.argiecommerce.model.Product
import com.example.argiecommerce.utils.ImageUtils
import com.example.argiecommerce.utils.Utils

class HomeFragment : Fragment(), View.OnClickListener, DemoAdapter.DemoAdapterOnClickListener, DemoAdapter2.DemoAdapterOnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var categoryItemList: ArrayList<CategoryItem>
    private lateinit var flashSaleProductList: ArrayList<Product>
    private lateinit var ocopProductList: ArrayList<Product>
    private lateinit var specialtyProductList: ArrayList<Product>
    private lateinit var recentProductList: ArrayList<Product>
    private lateinit var suggestedProductList: ArrayList<Product>

    private lateinit var rvCategory: RecyclerView
    private lateinit var rvFlashSaleProduct: RecyclerView
    private lateinit var rvOcopProduct: RecyclerView
    private lateinit var rvSpecialtyProduct: RecyclerView
    private lateinit var rvRecentProduct: RecyclerView
    private lateinit var rvSuggestedProduct: RecyclerView

    private lateinit var imageList: Array<Int>
    private lateinit var titleList: Array<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        imageList = ImageUtils.getImages.getCategoryItem()
        titleList = Utils.getText.getCategoryItemTitle()

        rvCategory = binding.content.listOfCategory
        rvCategory.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        rvCategory.setHasFixedSize(true)
        categoryItemList = arrayListOf()
        getCategoryData()

        rvSuggestedProduct = binding.content.listOfSuggestedProduct
        rvSuggestedProduct.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        rvSuggestedProduct.setHasFixedSize(true)
        suggestedProductList = arrayListOf()
        getSuggestedProduct()

        rvFlashSaleProduct = binding.content.listOfFlashSale
        rvFlashSaleProduct.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        rvFlashSaleProduct.setHasFixedSize(true)
        flashSaleProductList = arrayListOf()
        getFlashSaleProduct()

        rvOcopProduct = binding.content.listOfOcopProduct
        rvOcopProduct.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        rvOcopProduct.setHasFixedSize(true)
        ocopProductList = arrayListOf()
        getOcopProduct()

        rvSpecialtyProduct = binding.content.listOfSpecialtyProduct
        rvSpecialtyProduct.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        rvSpecialtyProduct.setHasFixedSize(true)
        specialtyProductList = arrayListOf()
        getSpecialtyProduct()

        rvRecentProduct = binding.content.listOfRecentProduct
        rvRecentProduct.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        rvRecentProduct.setHasFixedSize(true)
        recentProductList = arrayListOf()
        getRecentProduct()


        setFlipImages(ImageUtils.getImages.getSlides());

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


        setUpViews();
    }

    private fun setFlipImages(images: List<Int>) {
        for (image in images) {
            val imageView = ImageView(requireContext())
            imageView.setBackgroundResource(image)
            binding.content.imageSlider.addView(imageView)
        }

        binding.content.imageSlider.flipInterval = 2000
        binding.content.imageSlider.isAutoStart = true
        binding.content.imageSlider.setInAnimation(requireContext(), R.anim.slide_in_right)
        binding.content.imageSlider.setOutAnimation(requireContext(), R.anim.slide_out_left)
    }

    private fun setUpViews() {

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

    }

    private fun goToRecentProductFragment() {

    }

    private fun goToSpecialtyFragment() {

    }

    private fun goToStandardFragment() {

    }

    private fun goToFlashSaleFragment() {

    }

    private fun goToSearchFragment() {
        navController.navigate(R.id.action_homeFragment_to_searchFragment)
    }

    private fun getCategoryData() {
        for (i in imageList.indices) {
            val categoryItem = CategoryItem(imageList[i], titleList[i])
            categoryItemList.add(categoryItem)
        }
        binding.content.listOfCategory.adapter = CategoryAdapter(categoryItemList)
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

        binding.content.listOfSuggestedProduct.adapter = DemoAdapter(suggestedProductList, this)
    }

    private fun getRecentProduct() {

        binding.content.listOfRecentProduct.adapter = DemoAdapter2(suggestedProductList, this)
    }

    private fun getSpecialtyProduct() {
        binding.content.listOfSpecialtyProduct.adapter = DemoAdapter2(suggestedProductList, this)
    }

    private fun getOcopProduct() {
        binding.content.listOfOcopProduct.adapter = DemoAdapter2(suggestedProductList, this)
    }

    private fun getFlashSaleProduct() {
        binding.content.listOfFlashSale.adapter = DemoAdapter2(suggestedProductList, this)
    }

    override fun onClick(product: Product) {
        val action =
            com.example.argiecommerce.view.home.HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                product
            )
        navController.navigate(action)
    }
}