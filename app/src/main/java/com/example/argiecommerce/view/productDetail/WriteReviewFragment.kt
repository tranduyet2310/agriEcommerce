package com.example.argiecommerce.view.productDetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentWriteReviewBinding
import com.example.argiecommerce.model.ReviewRequest
import com.example.argiecommerce.model.ReviewResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.ReviewViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal


class WriteReviewFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentWriteReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }
    private val reviewViewModel: ReviewViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ReviewViewModel::class.java)
    }

    private val args: WriteReviewFragmentArgs by navArgs()
    private var user: User? = null
    private lateinit var alertDialog: AlertDialog
    private var productId: Long = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWriteReviewBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.write_review)

        user = userViewModel.user
        productId = args.productId

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.btnSubmit.setOnClickListener(this)
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnSubmit -> submitReview()
        }
    }

    private fun submitReview() {
        val token = loginUtils.getUserToken()
        val content = binding.editFeedback.text.toString().trim()
        val valueRating = binding.rateProduct.rating.toDouble()
        val rating: BigDecimal = BigDecimal.valueOf(valueRating)

        if (content.isEmpty() || valueRating == 0.0){
            showSnackbar(getString(R.string.field_required))
            return
        }

        val reviewRequest = ReviewRequest(content, rating)
        reviewViewModel.createReviews(token, user!!.id, productId, reviewRequest).observe(
            requireActivity(), { state -> processWriteReview(state) }
        )
    }

    private fun processWriteReview(state: ScreenState<ReviewResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    showSnackbar(getString(R.string.review_success))
                    navController.navigateUp()
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    displayErrorSnackbar(state.message)
                }
            }
        }
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction(getString(R.string.retry_v2)) { dismiss() } }
            .show()
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
    }

}