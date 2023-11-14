package com.hn_2452.shoes_nike.ui.home.offer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.databinding.FragmentOfferDetailBinding
import com.hn_2452.shoes_nike.utility.toTimeString

class OfferDetailFragment : BaseFragment<FragmentOfferDetailBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOfferDetailBinding.inflate(inflater, container, false)

    private val mArgs : OfferDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.toolbar)
        setupOfferDetail(mArgs.offer)
    }
    @SuppressLint("SetTextI18n")
    private fun setupOfferDetail(offer: Offer?) {
        offer?.let {
            mBinding?.apply {
                tvTitle.text = offer.title
                tvSubtitle.text = offer.subTitle
                tvDescription.text = offer.description
                tvAppliedTime.text = "Apply from ${offer.startTime.toTimeString()} to ${offer.endTime.toTimeString()}"
                btnGetOffer.setOnClickListener {
                    // TODO(get offer)
                }
            }
        }
    }

}