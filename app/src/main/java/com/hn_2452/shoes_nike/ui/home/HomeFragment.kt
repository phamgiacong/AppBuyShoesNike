package com.hn_2452.shoes_nike.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentHomeBinding
import com.hn_2452.shoes_nike.ui.home.offer.OfferAdapter
import com.hn_2452.shoes_nike.utility.GridSpacingItemDecoration
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.dpToPx
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        private const val TAG = "Nike:HomeFragment: "
    }

    private val mHomeViewModel: HomeViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupShoesTypeList()
        setupPopularShoesList()
        setupOfferList()
        setupNavigateToNotificationScreen()
    }

    private fun setupNavigateToNotificationScreen() {
        mBinding?.imvNotification?.setOnClickListener {
            mNavController?.navigate(R.id.notificationFragment)
        }
    }

    private fun setupOfferList() {
        mHomeViewModel.getAvailableOffer().observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        result.data?.let { offerList ->
                            mBinding?.viewpagerOffer?.adapter = OfferAdapter(this, offerList)
                            autoRunOfferBanner(offerList.size)
                        }
                    }

                    Status.ERROR -> {

                    }
                }
            }
        }
    }

    private fun autoRunOfferBanner(size: Int) {
        val currentIndex = mBinding?.viewpagerOffer?.currentItem
        currentIndex?.let {
            val index: Int? = if (currentIndex < size - 1) {
                mBinding?.viewpagerOffer?.currentItem?.plus(1)
            } else {
                0
            }
            index?.let {
                mBinding?.viewpagerOffer?.setCurrentItem(index, false)
                Handler(Looper.getMainLooper()).postDelayed({
                    autoRunOfferBanner(size)
                }, 4000L)
            }
        }

    }

    private fun setupShoesTypeList() {
        val controller = ShoesTypeAdapterController()
        mBinding?.rcvShoesType?.setController(controller)
        mHomeViewModel.getShoesType().observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        controller.setData(result.data)
                    }

                    Status.ERROR -> {

                    }
                }
            }
        }
    }

    private fun setupPopularShoesList() {
        val controller = ShoesAdapterController()
        mBinding?.rcvPopularShoes?.setController(controller)
        mBinding?.rcvPopularShoes?.addItemDecoration(
            GridSpacingItemDecoration(2, dpToPx(requireContext(), 17), false)
        )
        mHomeViewModel.getPopularShoes().observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        controller.setData(result.data)
                    }

                    Status.ERROR -> {

                    }
                }
            }
        }
    }

}