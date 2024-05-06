package com.example.argiecommerce.view.profile

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.CooperationAdapter
import com.example.argiecommerce.databinding.FragmentCooperationBinding
import com.example.argiecommerce.model.CooperationResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants.COOPERATION_KEY
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.CooperationViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar


class CooperationFragment : Fragment() {
    private lateinit var binding: FragmentCooperationBinding
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val cooperationViewModel: CooperationViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CooperationViewModel::class.java)
    }

    private lateinit var cooperationAdapter: CooperationAdapter
    private val cooperationList: ArrayList<CooperationResponse> = arrayListOf()
    private var user: User? = null
    private lateinit var alertDialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCooperationBinding.inflate(inflater, container, false)

        user = userViewModel.user
        setupRecyclerView()
        getCooperationData()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        cooperationAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable(COOPERATION_KEY, it)
            }
            navController.navigate(R.id.action_cooperationFragment_to_cooperationDetailFragment, b)
        }
    }
    private fun getCooperationData() {
        cooperationViewModel.getCooperationByUserId(user!!.id).observe(
            requireActivity(), { state -> processCooperationResponse(state) }
        )
    }

    private fun setupRecyclerView() {
        cooperationAdapter = CooperationAdapter(cooperationList)
        binding.rcvCultivation.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = cooperationAdapter
        }
    }

    private fun showImagePlaceholder() {
        binding.apply {
            imgPlaceholder.visibility = View.VISIBLE
            rcvCultivation.visibility = View.GONE
            headerLayout.visibility = View.GONE
            tvPlaceholder.visibility = View.VISIBLE
        }
    }

    private fun hideImagePlaceholder() {
        binding.apply {
            imgPlaceholder.visibility = View.GONE
            rcvCultivation.visibility = View.VISIBLE
            headerLayout.visibility = View.VISIBLE
            tvPlaceholder.visibility = View.GONE
        }
    }

    private fun processCooperationResponse(state: ScreenState<ArrayList<CooperationResponse>?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    if (state.data.isEmpty()){
                        showImagePlaceholder()
                    } else {
                        hideImagePlaceholder()
                        cooperationList.clear()
                        cooperationList.addAll(state.data)
                        cooperationAdapter.notifyDataSetChanged()
                    }
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    showSnackbar(state.message)
                }
            }
        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }

}