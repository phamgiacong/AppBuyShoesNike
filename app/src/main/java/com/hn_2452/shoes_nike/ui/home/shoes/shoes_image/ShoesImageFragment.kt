package com.hn_2452.shoes_nike.ui.home.shoes.shoes_image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentShoesImageBinding

class ShoesImageFragment : BaseFragment<FragmentShoesImageBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShoesImageBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding?.imvShoesImage?.load(
            arguments?.getString("shoes_image")
        ) {
            placeholder(R.color.background_secondary_color)
            error(R.drawable.shoes_placehoder)
        }
    }
}