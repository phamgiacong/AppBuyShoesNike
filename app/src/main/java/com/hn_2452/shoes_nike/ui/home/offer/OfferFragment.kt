package com.hn_2452.shoes_nike.ui.home.offer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.databinding.FragmentOfferBinding
import com.hn_2452.shoes_nike.ui.home.HomeFragmentDirections

@Suppress("DEPRECATION")
class OfferFragment : BaseFragment<FragmentOfferBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOfferBinding.inflate(inflater)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomBar(true)
        val offer = arguments?.getParcelable<Offer>("offer")
        offer?.let { offer ->
            mBinding?.apply {
                imvOfferImage.load(offer.image) {
                    error(R.drawable.ic_launcher_background)
                }

                root.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToOfferDetailFragment(offer)
                    mNavController?.navigate(action)
                }
            }
        }
    }
}