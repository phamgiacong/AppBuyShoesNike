package com.hn_2452.shoes_nike.ui.home.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.ui.home.offer.OfferFragment

class OfferAdapter(
    mFragment: Fragment,
    private val mOfferList: List<Offer>
): FragmentStateAdapter(mFragment) {
    override fun getItemCount(): Int {
        return mOfferList.size
    }
    override fun createFragment(position: Int): Fragment {
        val offer = mOfferList[position]
        return OfferFragment().apply {
            arguments = Bundle()
            arguments?.putParcelable("offer", offer)
        }
    }

}