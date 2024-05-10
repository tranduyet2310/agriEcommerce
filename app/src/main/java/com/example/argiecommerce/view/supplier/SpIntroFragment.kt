package com.example.argiecommerce.view.supplier

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentSuppilerIntroBinding
import com.example.argiecommerce.model.Image
import com.example.argiecommerce.model.SupplierBasicInfo
import com.example.argiecommerce.model.SupplierIntroResponse
import com.example.argiecommerce.utils.Constants.BANNER
import com.example.argiecommerce.utils.Constants.INFO_GARDEN
import com.example.argiecommerce.utils.Constants.INFRO_SHOP
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.SupplierViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class SpIntroFragment : Fragment() {
    private lateinit var binding: FragmentSuppilerIntroBinding
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val supplierViewModel: SupplierViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SupplierViewModel::class.java)
    }
    private var supplierBasicInfo: SupplierBasicInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuppilerIntroBinding.inflate(inflater)

        supplierBasicInfo = userViewModel.supplierBasicInfo
        if (supplierBasicInfo != null) {
            getSupplierIntroInfo()
        }

        return binding.root
    }

    private fun setupView() {
        binding.tvGardenInfo.text = getString(R.string.no_intro_garden)
        binding.tvGardenInfo.gravity = Gravity.CENTER
        binding.tvShopInfo.text = getString(R.string.no_intro_shop)
        binding.tvShopInfo.gravity = Gravity.CENTER
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(BANNER))
        binding.imageShopIntro.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
        binding.imageGardenIntro.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
    }

    private fun getSupplierIntroInfo() {
        supplierViewModel.getSupplierIntroInfo(supplierBasicInfo!!.supplierId).observe(
            requireActivity(), { state -> processGetSupplierInfo(state) }
        )
    }

    private fun setupShopImages(images: MutableList<Image>) {
        val imageList = ArrayList<SlideModel>()
        if (images.isEmpty()) {
            imageList.add(SlideModel(BANNER))
        } else {
            for (image in images) {
                val modifiedUrl = image.imageUrl.replace("http://", "https://")
                imageList.add(SlideModel(modifiedUrl))
            }
        }
        binding.imageShopIntro.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
    }

    private fun setupGardenImages(images: MutableList<Image>) {
        val imageList = ArrayList<SlideModel>()
        if (images.isEmpty()) {
            imageList.add(SlideModel(BANNER))
        } else {
            for (image in images) {
                val modifiedUrl = image.imageUrl.replace("http://", "https://")
                imageList.add(SlideModel(modifiedUrl))
            }
        }
        binding.imageGardenIntro.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
    }

    private fun processGetSupplierInfo(state: ScreenState<ArrayList<SupplierIntroResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    binding.progressBar.visibility = View.GONE
                    if (state.data.isEmpty()) setupView()
                    else
                        for (response in state.data) {
                            if (response.type.equals(INFRO_SHOP)) {
                                binding.tvShopInfo.text = response.description
                                setupShopImages(response.images)
                            }
                            if (response.type.equals(INFO_GARDEN)) {
                                binding.tvGardenInfo.text = response.description
                                setupGardenImages(response.images)
                            }
                        }
                }
            }

            is ScreenState.Error -> {
                binding.progressBar.visibility = View.GONE
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