package com.hn_2452.shoes_nike.ui.home.offer

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.model.UserOffer
import com.hn_2452.shoes_nike.databinding.FragmentOfferDetailBinding
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import com.hn_2452.shoes_nike.utility.handleResource
import com.hn_2452.shoes_nike.utility.toDayString
import com.hn_2452.shoes_nike.utility.toVND
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferDetailFragment : BaseFragment<FragmentOfferDetailBinding>() {

    companion object {
        const val TAG = "Nike:OfferDetailFragment: "
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOfferDetailBinding.inflate(inflater, container, false)

    private val mArgs: OfferDetailFragmentArgs by navArgs()
    private var mAvailableToGetOffer = false

    private val mOfferViewModel: OfferViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.toolbar)
        Log.i(TAG, "getOfferOfUser: " + mArgs.offer)
        setupOfferDetail(mArgs.offer)
        getOfferOfUser()

        if (getStringDataByKey(requireContext(), TOKEN).isNotEmpty()) {
            Log.i(TAG, "onViewCreated: ")
            mBinding?.btnGetOffer?.text = getString(R.string.loading)
        }

    }


    private fun getOfferOfUser() {
        Log.i(TAG, "getOfferOfUser: ")
        handleResource(
            data = mOfferViewModel.getOfferOfUser(),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            onSuccess = { userOffers ->
                if (userOffers != null && isHasOffer(userOffers, mArgs.offer)) {
                    informHaveReceiveOffer()
                } else {
                    mBinding?.btnGetOffer?.text = getString(R.string.get_offer)
                    mAvailableToGetOffer = true
                }
            },
            onError = {
                Log.e(TAG, "getOfferOfUser: $it", )
            }
        )
    }

    private fun isHasOffer(userOffer: List<UserOffer>, offer: Offer): Boolean {
        var res : UserOffer?
        try {
             res = userOffer.find {
                it.offer.id == offer.id
            }
            return res != null
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setupOfferDetail(offer: Offer?) {
        offer?.let {
            mBinding?.apply {

                val saleMore = if (offer.maxDiscount != -1L) {
                    "Giảm tối đa ${offer.maxDiscount.toVND()} cho đơn hàng từ ${offer.valueToApply.toVND()}"
                } else {
                    "Áp dụng cho đơn hàng từ ${offer.valueToApply.toVND()}"
                }

                tvSaleMore.text = saleMore

                tvTitle.text = offer.title
                tvDescription.text = offer.description
                tvAppliedTime.text =
                    "Áp dụng từ ${offer.startTime.toDayString()} đến ${offer.endTime.toDayString()}"
                imvOffer.load(offer.image) {
                    error(R.drawable.ic_launcher_background)
                }
                btnGetOffer.setOnClickListener {
                    if (getStringDataByKey(requireContext(), TOKEN).isEmpty()) {
                        mNavController?.navigate(R.id.loginFragment)
                    } else {
                        if (mAvailableToGetOffer) {
                            addOffer(offer.id)
                        }
                    }
                }
            }
        }
    }

    private fun addOffer(id: String) {
        Log.i(TAG, "addOffer: ")
        handleResource(
            data = mOfferViewModel.addOffer(id),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            onSuccess = {
                Log.i(TAG, "addOffer: success")
                informHaveReceiveOffer()
            },
            isErrorInform = true
        )
    }

    private fun informHaveReceiveOffer() {
        Log.i(TAG, "informHaveReceiveOffer: ")
        mAvailableToGetOffer = false
        mBinding?.btnGetOffer?.text = getString(R.string.have_receive)
    }

}