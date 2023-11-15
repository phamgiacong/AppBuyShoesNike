package com.hn_2452.shoes_nike.ui.home.shoes.shoes_image

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ShoesImageAdapter(
    fragment: Fragment,
    private val images: List<String>
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return images.size
    }

    override fun createFragment(position: Int): Fragment {
        val image = images[position]
        return ShoesImageFragment().apply {
            arguments = Bundle()
            arguments?.putString("shoes_image", image)
        }
    }
}