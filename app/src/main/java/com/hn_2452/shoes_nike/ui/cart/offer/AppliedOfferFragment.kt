package com.hn_2452.shoes_nike.ui.cart.offer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentListPromoBinding
import com.hn_2452.shoes_nike.ui.cart.check_out.CheckOutViewModel
import com.hn_2452.shoes_nike.utility.handleResource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppliedOfferFragment : BaseFragment<FragmentListPromoBinding>() {

    private val mAppliedOfferViewModel: AppliedOfferViewModel by viewModels()

    private val mCheckOutViewModel: CheckOutViewModel by activityViewModels()

    @Inject
    lateinit var mOfferAdapter: OfferAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentListPromoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.toolBar)
        setupOfferList()
    }

    private fun setupOfferList() {
        mOfferAdapter.mPrice = mCheckOutViewModel.mPrice
        mOfferAdapter.mOnDetail = { offer ->
            mNavController?.navigate(
                AppliedOfferFragmentDirections.actionAppliedOfferFragmentToOfferDetailFragment(offer)
            )
        }
        mOfferAdapter.mOnClick = { offer ->
            mCheckOutViewModel.mCurrentOffer.value = offer
            mNavController?.popBackStack()
        }
        mBinding?.rcvPromoList?.adapter = mOfferAdapter
        loadOfferList()
    }

    private fun loadOfferList() {
        handleResource(
            data = mAppliedOfferViewModel.getOfferOfUser(),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            onSuccess = { offers ->
                offers?.run {
                    mOfferAdapter.submitList(offers)
                }
            },
            isErrorInform = true
        )
    }

}