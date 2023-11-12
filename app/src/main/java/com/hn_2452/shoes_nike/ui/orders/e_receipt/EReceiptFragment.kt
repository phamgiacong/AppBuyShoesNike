package com.hn_2452.shoes_nike.ui.orders.e_receipt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentEReceiptBinding

class EReceiptFragment : BaseFragment<FragmentEReceiptBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentEReceiptBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}