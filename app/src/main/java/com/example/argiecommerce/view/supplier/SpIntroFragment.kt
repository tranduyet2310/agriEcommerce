package com.example.argiecommerce.view.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentSuppilerIntroBinding

class SpIntroFragment : Fragment() {
    private lateinit var binding: FragmentSuppilerIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuppilerIntroBinding.inflate(inflater)

        setupShopImages();
        setupGardenImages();

        return binding.root
    }

    private fun setupShopImages() {
        val imageList = ArrayList<SlideModel>();
        imageList.add(SlideModel("https://res.cloudinary.com/dtdctll9c/image/upload/v1710340597/buupvmd6jmb1aofikr9n.png"))
        imageList.add(SlideModel("https://bit.ly/2YoJ77H"))
        imageList.add(SlideModel("https://bit.ly/2BteuF2"))

        binding.imageShopIntro.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
    }

    private fun setupGardenImages() {
        val imageList = ArrayList<SlideModel>();
        imageList.add(SlideModel("https://res.cloudinary.com/dtdctll9c/image/upload/v1710340597/buupvmd6jmb1aofikr9n.png"))
        imageList.add(SlideModel("https://bit.ly/2YoJ77H"))
        imageList.add(SlideModel("https://bit.ly/2BteuF2"))

        binding.imageGardenIntro.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}