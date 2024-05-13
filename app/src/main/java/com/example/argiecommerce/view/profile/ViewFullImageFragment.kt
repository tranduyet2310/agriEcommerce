package com.example.argiecommerce.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentViewFullImageBinding
import com.example.argiecommerce.utils.GlideApp


class ViewFullImageFragment : Fragment() {
    private lateinit var binding: FragmentViewFullImageBinding
    private lateinit var navController: NavController

    private val navArgs: ViewFullImageFragmentArgs by navArgs()
    private lateinit var imageUrl: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentViewFullImageBinding.inflate(inflater)
        binding.toolbarLayout.titleToolbar.text = "Chi tiết ảnh"

        imageUrl = navArgs.image
        showImage()

        return binding.root
    }

    private fun showImage() {
        GlideApp.with(requireContext()).load(imageUrl)
            .placeholder(R.drawable.image_gallery)
            .error(R.drawable.image_gallery)
            .into(binding.image)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }
}