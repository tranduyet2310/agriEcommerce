package com.example.argiecommerce.view

import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.DetailContentBinding
import com.example.argiecommerce.databinding.FragmentDetailsBinding
import com.example.argiecommerce.model.Product


class DetailsFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    val args: DetailsFragmentArgs by navArgs()

    private var isExpanded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val product: Product = args.productValue
        binding.details.imageOfProduct.setImageResource(R.drawable.product_demo)
        binding.details.priceOfProduct.text = String.format("%,.0f", product.productPrice)
        binding.details.priceOfProductDiscount.text = String.format("%,.0f", product.productPrice)
        binding.details.tvRatingMiniView.text = "4.5"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.buy.setOnClickListener(this);
        binding.addToCart.setOnClickListener(this);
        binding.details.writeReview.setOnClickListener(this)
        binding.details.tvSeeAllReviews.setOnClickListener(this)
        binding.details.btnSeeMoreInfoProduct.setOnClickListener(this)
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
        }
    }

    private fun seeMoreInfoProduct() {
        if (isExpanded){
            binding.details.tvInfoDetailsOfProduct.maxLines = 5
            binding.details.tvInfoDetailsOfProduct.ellipsize = TextUtils.TruncateAt.END
            binding.details.btnSeeMoreInfoProduct.text = buildString {
                append("Xem thêm")
            }
            isExpanded = false
        } else {
            binding.details.tvInfoDetailsOfProduct.maxLines = Int.MAX_VALUE
            binding.details.tvInfoDetailsOfProduct.ellipsize = null
            binding.details.btnSeeMoreInfoProduct.text = buildString {
                append("Thu gọn")
            }
            isExpanded = true
        }
    }

    private fun seeAllReviews() {
        navController.navigate(R.id.action_detailsFragment_to_allReviewsFragment)
    }

    private fun writeReview() {
        navController.navigate(R.id.action_detailsFragment_to_writeReviewFragment)
    }

    private fun addProductToCart() {
        Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun buyProduct() {
        navController.navigate(R.id.action_detailsFragment_to_billingFragment)
    }


}