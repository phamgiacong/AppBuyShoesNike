package com.hn_2452.shoes_nike.ui.orders

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hn_2452.shoes_nike.ui.orders.active.OrderActiveFragment
import com.hn_2452.shoes_nike.ui.orders.completed.OrderCompletedFragment

class ViewPagerOrderAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderActiveFragment()
            1 -> OrderCompletedFragment()
            else -> OrderActiveFragment()
        }
    }
}