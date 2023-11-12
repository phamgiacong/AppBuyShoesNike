package com.hn_2452.shoes_nike.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentHomeBinding
import com.hn_2452.shoes_nike.di.LocalService
import com.hn_2452.shoes_nike.ui.home.offer.OfferAdapter
import com.hn_2452.shoes_nike.utility.GridSpacingItemDecoration
import com.hn_2452.shoes_nike.utility.dpToPx

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        private const val TAG = "Nike:HomeFragment: "
    }

    private val mHomeViewModel by lazy {
        LocalService.createHomeViewModel(this)
    }

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
        mHomeViewModel.mAvailableOffer.observe(viewLifecycleOwner) { offerList ->
            offerList?.let {
                Log.i(TAG, "setupOfferList: $offerList")
                mBinding?.viewpagerOffer?.adapter = OfferAdapter(this, offerList)
                autoRunOfferBanner(offerList.size)
            }
        }
        mHomeViewModel.getAvailableOffer()
    }

    private fun autoRunOfferBanner(size: Int) {
        val currentIndex = mBinding?.viewpagerOffer?.currentItem
        currentIndex?.let {
            val index: Int? = if (currentIndex < size -1) {
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
        mHomeViewModel.mShoesType.observe(viewLifecycleOwner) { shoesType ->
            shoesType?.let {
                controller.setData(it)
            }
        }
        mHomeViewModel.getShoesType()
    }

    private fun setupPopularShoesList() {
        val controller = ShoesAdapterController()
        mBinding?.rcvPopularShoes?.setController(controller)
        mBinding?.rcvPopularShoes?.addItemDecoration(
            GridSpacingItemDecoration(2, dpToPx(requireContext(), 17), false)
        )
        mHomeViewModel.mShoes.observe(viewLifecycleOwner) { shoesList ->
            shoesList?.let {
                controller.setData(shoesList)
            }
        }
        mHomeViewModel.getPopularShoes()
    }

}