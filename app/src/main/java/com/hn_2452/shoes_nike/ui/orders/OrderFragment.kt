package com.hn_2452.shoes_nike.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentOrderBinding

class OrderFragment : BaseFragment<FragmentOrderBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOrderBinding.inflate(inflater, container, false)

}