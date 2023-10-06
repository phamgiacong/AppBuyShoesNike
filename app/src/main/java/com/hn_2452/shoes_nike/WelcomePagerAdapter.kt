package com.hn_2452.shoes_nike

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hn_2452.shoes_nike.fragment_welcome.Welcome1Fragment
import com.hn_2452.shoes_nike.fragment_welcome.Welcome2Fragment
import com.hn_2452.shoes_nike.fragment_welcome.Welcome3Fragment
import com.hn_2452.shoes_nike.fragment_welcome.WelcomeFragment

class WelcomePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeFragment()
            1 -> Welcome1Fragment()
            2 -> Welcome2Fragment()
            3 -> Welcome3Fragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        return 4 // Số lượng trang trong ViewPager
    }
}