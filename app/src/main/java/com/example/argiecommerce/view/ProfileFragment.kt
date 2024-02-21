package com.example.argiecommerce.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentProfileBinding
import com.example.argiecommerce.model.User


class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        // demo
//        user = User("client", "demo@gmail.com", "0123456")
        binding.linearLogOut.setOnClickListener(this)
        binding.switchNotification.setOnClickListener(this)
        binding.constraintProfile.setOnClickListener(this)
        binding.linearAllOrders.setOnClickListener(this)
        binding.linearLocation.setOnClickListener(this)
        binding.linearTrackOrder.setOnClickListener(this)
        binding.linearBilling.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.linearLogOut -> logOut()
            R.id.switchNotification -> notificationFunc()
            R.id.constraintProfile -> changeInfoAccount()
            R.id.linearLocation -> changeLocation()
            R.id.linearAllOrders -> seeAllOrders()
            R.id.linearTrackOrder -> seeTrackOrder()
            R.id.linearBilling -> billingProduct()
        }
    }

    private fun billingProduct() {
        Toast.makeText(requireContext(), "Cliked", Toast.LENGTH_SHORT).show()
    }

    private fun seeTrackOrder() {
        Toast.makeText(requireContext(), "Cliked", Toast.LENGTH_SHORT).show()
    }

    private fun seeAllOrders() {
        Toast.makeText(requireContext(), "Cliked", Toast.LENGTH_SHORT).show()
    }

    private fun changeLocation() {
        Toast.makeText(requireContext(), "Cliked", Toast.LENGTH_SHORT).show()
    }

    private fun changeInfoAccount() {
        Toast.makeText(requireContext(), "Cliked", Toast.LENGTH_SHORT).show()
    }

    private fun notificationFunc() {
        Toast.makeText(requireContext(), "Cliked", Toast.LENGTH_SHORT).show()
    }

    private fun logOut() {
        navController.navigate(R.id.action_profileFragment_to_loginFragment)
    }


}