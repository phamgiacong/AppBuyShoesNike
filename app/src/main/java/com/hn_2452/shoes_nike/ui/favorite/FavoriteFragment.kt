package com.hn_2452.shoes_nike.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentFavoriteBinding

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavoriteBinding.inflate(inflater, container, false)

}