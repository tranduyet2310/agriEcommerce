package com.example.argiecommerce.view.specialty

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.VerticalProductAdapter
import com.example.argiecommerce.databinding.FragmentBaseSpecialtyBinding
import com.example.argiecommerce.utils.Constants.PRODUCT_KEY


open class BaseSpecialtyFragment : Fragment(R.layout.fragment_base_specialty) {

    private lateinit var binding: FragmentBaseSpecialtyBinding
    protected val productAdapter: VerticalProductAdapter by lazy {
        VerticalProductAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseSpecialtyBinding.inflate(inflater)

        binding.allProductRecyclerView.apply {
            layoutManager = GridLayoutManager(
                requireContext(), 2,
                GridLayoutManager.VERTICAL, false
            )
            adapter = productAdapter
            setHasFixedSize(true)
        }

        setupAreaLayout()
        return binding.root
    }

    open fun setupAreaData() {
        binding.imageArea.setImageResource(R.drawable.tay_bac)
        binding.areaInfo.text = requireContext().resources.getString(R.string.tay_bac_bo)
    }

    private fun setupAreaLayout() {
        binding.layouts.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.expandable.setOnClickListener {
            val visibility =
                if (binding.imageArea.visibility == View.GONE) View.VISIBLE else View.GONE
            binding.imageArea.visibility = visibility
            binding.scrollAreaInfo.visibility = visibility
            binding.titleProductInArea.visibility = visibility
            binding.scrollProductArea.visibility = visibility
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter.onClick = {
            val b = Bundle().apply { putParcelable(PRODUCT_KEY, it) }
            findNavController().navigate(R.id.action_specialtyFragment_to_detailsFragment, b)
        }
    }

}