package com.hn_2452.shoes_nike.ui.review_shoes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentShoesReviewBinding
import com.hn_2452.shoes_nike.utility.handleResource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShoesReviewFragment : BaseFragment<FragmentShoesReviewBinding>() {

    companion object {

    }

    @Inject
    lateinit var mStarAdapter: StarAdapter

    private val mShoesReviewViewModel: ShoesReviewViewModel by viewModels()

    private lateinit var mShoesReviewController: ShoesReviewController

    private val mArgs: ShoesReviewFragmentArgs by navArgs()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShoesReviewBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.materialToolbar)
        val shoesId = mArgs.shoesId
        loadShoesReview(shoesId)
        setupReviewList()
        setupStarList()
    }

    private fun setupStarList() {
        mStarAdapter = StarAdapter()
        val starList = mutableListOf<Pair<Int, Boolean>>()
        mShoesReviewViewModel.mStarList.forEach {
            if (it == -1) {
                starList.add(Pair(it, true))
            } else {
                starList.add(Pair(it, false))
            }
        }
        mStarAdapter.setData(starList)
        mStarAdapter.setOnClick { selected ->

            if (selected == -1) {
                mShoesReviewController.setData(mShoesReviewViewModel.mReviewList)
            } else {
                mShoesReviewController.setData(
                    mShoesReviewViewModel.mReviewList?.filter {
                        it.rate >= selected && it.rate < (selected + 1.0f)
                    }
                )
            }

            val updateStartList = mutableListOf<Pair<Int, Boolean>>()
            mShoesReviewViewModel.mStarList.forEach { star ->
                if (selected == star) {
                    updateStartList.add(Pair(star, true))
                } else {
                    updateStartList.add(Pair(star, false))
                }
            }
            mStarAdapter.setData(updateStartList)
        }

        mBinding?.rcvStart?.adapter = mStarAdapter
    }

    private fun setupReviewList() {
        mShoesReviewController = ShoesReviewController()
        mBinding?.shoesReviewList?.setController(mShoesReviewController)
    }

    private fun loadShoesReview(shoesId: String) {
        handleResource(
            data = mShoesReviewViewModel.getShoesReview(shoesId),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            onSuccess = { shoesReviews ->
                mShoesReviewViewModel.mReviewList = shoesReviews
                mShoesReviewController.setData(shoesReviews)
            }
        )
    }


}