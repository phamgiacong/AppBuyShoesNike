package com.hn_2452.shoes_nike.ui.welcome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

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

    override fun getCount() = 4
}