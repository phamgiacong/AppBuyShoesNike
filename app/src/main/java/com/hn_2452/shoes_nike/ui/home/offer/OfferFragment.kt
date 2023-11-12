package com.hn_2452.shoes_nike.ui.home.offer

import android.annotation.SuppressLint
import android.graphics.Color
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
        val offer = arguments?.getParcelable<Offer>("offer")
        offer?.let { offer ->
            mBinding?.apply {
                root.setBackgroundColor(Color.parseColor(offer.imageBackground))
                tvTitle.text = offer.title
                tvSubtitle.text = offer.subTitle
                if (offer.discountUnit == 1) {
                    tvDiscount.text = "${offer.discount}%"
                } else {
                    tvDiscount.text = "${offer.discount} USD"
                }
                imvOffer.load(BASE_URL + offer.image) {
                    error(R.drawable.shoes_placehoder)
                }

                root.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToOfferDetailFragment()
                    action.offer = offer
                    mNavController?.navigate(action)
                }
            }
        }
    }
}