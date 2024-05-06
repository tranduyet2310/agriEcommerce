package com.example.argiecommerce.view.productDetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.OrderLoadingAdapter
import com.example.argiecommerce.adapter.ReviewAdapter
import com.example.argiecommerce.databinding.FragmentAllReviewsBinding
import com.example.argiecommerce.model.ReviewStatisticResponse
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.ReviewViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AllReviewsFragment : Fragment() {
    private lateinit var binding: FragmentAllReviewsBinding
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private val reviewViewModel: ReviewViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ReviewViewModel::class.java)
    }
    private val reviewAdapter: ReviewAdapter by lazy {
        ReviewAdapter()
    }
    private val args: AllReviewsFragmentArgs by navArgs()
    private var user: User? = null
    private lateinit var alertDialog: AlertDialog
    private var productId: Long = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllReviewsBinding.inflate(inflater)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.reviews)

        user = userViewModel.user
        productId = args.productId

        setupRecyclerView()
        getReviews()
        getReviewStatistic()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun getReviewStatistic() {
        reviewViewModel.statisticRating(productId).observe(
            requireActivity(), { state -> processGetReviewStatistic(state) }
        )
    }

    private fun getReviews() {
        lifecycleScope.launch {
            reviewViewModel.getAllReivewsByProductId(productId).collectLatest { pagingData ->
                reviewAdapter.addLoadStateListener { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        reviewAdapter.itemCount < 1
                    ) {
                        hideRecyclerView()
                    } else {
                        showRecyclerView()
                    }
                }
                reviewAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.allReviewsList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = reviewAdapter.withLoadStateHeaderAndFooter(
                header = OrderLoadingAdapter { reviewAdapter.retry() },
                footer = OrderLoadingAdapter { reviewAdapter.retry() }
            )
        }
    }

    private fun processGetReviewStatistic(state: ScreenState<ReviewStatisticResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    setupRatingBar(state.data)
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

    private fun setupRatingBar(data: ReviewStatisticResponse) {
        val oneStar = data.oneStar.toInt()
        val twoStar = data.twoStar.toInt()
        val threeStar = data.threeStar.toInt()
        val fourStar = data.fourStar.toInt()
        val fiveStar = data.fiveStar.toInt()
        val totalRating = data.totalReviews.toInt()
        val averageRating = data.averageRating
        val totalReviews = "${data.totalReviews} Đánh giá"

        binding.tvTotalRating.text = totalReviews
        binding.tvAverageRating.text = averageRating.toDouble().toString()

        binding.progressBar1Star.progress = oneStar
        binding.progressBar1Star.max = totalRating
        binding.tvTotal1StarRating.text = oneStar.toString()

        binding.progressBar2Star.progress = twoStar
        binding.progressBar2Star.max = totalRating
        binding.tvTotal2StarRating.text = twoStar.toString()

        binding.progressBar3Star.progress = threeStar
        binding.progressBar3Star.max = totalRating
        binding.tvTotal3StarRating.text = threeStar.toString()

        binding.progressBar4Star.progress = fourStar
        binding.progressBar4Star.max = totalRating
        binding.tvTotal4StarRating.text = fourStar.toString()

        binding.progressBar5Star.progress = fiveStar
        binding.progressBar5Star.max = totalRating
        binding.tvTotal5StarRating.text = fiveStar.toString()
    }

    private fun hideRecyclerView() {
        binding.image.visibility = View.VISIBLE
        binding.tvMessage.visibility = View.VISIBLE
        binding.tvMessage2.visibility = View.VISIBLE
        binding.allReviewsList.visibility = View.GONE
        binding.ratingLayout.visibility = View.GONE
    }

    private fun showRecyclerView() {
        binding.image.visibility = View.GONE
        binding.tvMessage.visibility = View.GONE
        binding.tvMessage2.visibility = View.GONE
        binding.allReviewsList.visibility = View.VISIBLE
        binding.ratingLayout.visibility = View.VISIBLE
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
    }
}