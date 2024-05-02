package com.example.argiecommerce.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentCooperativeResultBinding
import com.example.argiecommerce.utils.Constants


class CooperativeResultFragment : Fragment() {
    private lateinit var binding: FragmentCooperativeResultBinding
    private lateinit var navController: NavController

    private val args: CooperativeResultFragmentArgs by navArgs()
    private var cooperativePaymentId: Long = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCooperativeResultBinding.inflate(inflater)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.result)

        cooperativePaymentId = args.cooperative

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.btnHome.setOnClickListener {
            navController.navigate(R.id.action_cooperativeResultFragment_to_cooperationFragment)
        }
        binding.tvSeeOrder.setOnClickListener {
            val b = Bundle().apply {
                putLong(Constants.COOPERATIIVE_KEY, cooperativePaymentId)
            }
            navController.navigate(R.id.action_cooperativeResultFragment_to_cooperativeDetailFragment, b)
        }
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }
}