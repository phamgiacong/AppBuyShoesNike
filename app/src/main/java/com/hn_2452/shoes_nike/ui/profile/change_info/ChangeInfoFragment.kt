package com.hn_2452.shoes_nike.ui.profile.change_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentChangeInfoBinding

class ChangeInfoFragment : BaseFragment<FragmentChangeInfoBinding>() {

    companion object {
        const val TAG = "Nike:ChangeInfoFragment: "
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentChangeInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}