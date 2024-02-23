package com.example.argiecommerce.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentBillingBinding
import com.example.argiecommerce.databinding.FragmentCartBinding


class BillingFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentBillingBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.buttonPlaceOrder.setOnClickListener(this)
        binding.imageAddAddress.setOnClickListener(this)
        binding.tvPaymentExplanation.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonPlaceOrder -> goToPaymentFragment()
            R.id.imageAddAddress -> goToAddressFragment()
            R.id.tvPaymentExplanation -> goToPaymentMethodFragment()
        }
    }

    private fun goToPaymentMethodFragment() {
        Toast.makeText(requireContext(), "Cliked", Toast.LENGTH_SHORT).show()
    }

    private fun goToAddressFragment() {
        navController.navigate(R.id.action_billingFragment_to_addressFragment)
    }

    private fun goToPaymentFragment() {
        Toast.makeText(requireContext(), "Cliked", Toast.LENGTH_SHORT).show()
    }
}