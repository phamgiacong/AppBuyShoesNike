package com.hn_2452.shoes_nike.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentOrderBinding

class OrderFragment : BaseFragment<FragmentOrderBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOrderBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.tabLayoutOrder.addTab(mBinding.tabLayoutOrder.newTab().setText("Active"))
        mBinding.tabLayoutOrder.addTab(mBinding.tabLayoutOrder.newTab().setText("Completed"))

        mBinding.viewPager2Order.adapter = ViewPagerOrderAdapter(childFragmentManager, lifecycle)

        mBinding.tabLayoutOrder.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    mBinding.viewPager2Order.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        mBinding.viewPager2Order.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mBinding.tabLayoutOrder.selectTab(mBinding.tabLayoutOrder.getTabAt(position))
            }
        })
    }

}