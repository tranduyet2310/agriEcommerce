package com.example.argiecommerce.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentAddressBinding
import com.example.argiecommerce.databinding.FragmentBillingBinding

class AddressFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.buttonDelelte.setOnClickListener(this)
        binding.buttonSave.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonSave -> goToAddressFragment()
            R.id.buttonDelelte -> goToAddressFragment()
        }
    }

    private fun goToAddressFragment() {
        Toast.makeText(requireContext(), "Cliked", Toast.LENGTH_SHORT).show()
    }
}