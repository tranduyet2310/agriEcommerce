package com.example.argiecommerce.view

import android.os.Bundle
import android.os.Parcelable
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

//        val innerLayout = LayoutInflater.from(requireContext()).inflate(R.layout.detail_content, null, false)
//        binding.root.addView(innerLayout)
//
////        val innerBinding = DetailContentBinding.inflate(inflater, null, false)
//
//        val product: Product = args.productValue
////        innerBinding.imageOfProduct.setImageResource(R.drawable.product_demo)
////        innerBinding.priceOfProduct.text = String.format("%,.0f", product.productPrice)
////        innerBinding.priceOfProductDiscount.text = String.format("%,.0f", product.productPrice)
////        innerBinding.tvRatingMiniView.text = "4.5"
//
//        binding.details?.imageOfProduct?.setImageResource(R.drawable.product_demo)
//        binding.details?.priceOfProduct?.text = String.format("%,.0f", product.productPrice)
//        binding.details?.priceOfProductDiscount?.text = String.format("%,.0f", product.productPrice)
//        binding.details?.tvRatingMiniView?.text = "4.5"

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

//        binding.buy.setOnClickListener(this);
//        binding.addToCart.setOnClickListener(this);
//        binding.details?.writeReview?.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buy -> buyProduct()
            R.id.addToCart -> addProductToCart()
            R.id.writeReview -> writeReview()
        }
    }

    private fun writeReview() {
        Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun addProductToCart() {
        Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun buyProduct() {
        Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
    }


}