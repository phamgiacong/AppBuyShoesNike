package com.hn_2452.shoes_nike.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentCartBinding

class CartFragment : BaseFragment<FragmentCartBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCartBinding.inflate(inflater, container, false)

}