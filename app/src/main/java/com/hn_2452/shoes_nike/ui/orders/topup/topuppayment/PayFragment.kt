package com.hn_2452.shoes_nike.ui.orders.topup.topuppayment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentMyEWalletBinding
import com.hn_2452.shoes_nike.databinding.FragmentPayBinding
import com.hn_2452.shoes_nike.ui.orders.e_wallet.MyEWalletViewModel

class PayFragment : Fragment() {

    private val viewModel: PayViewModel by viewModels()
    private lateinit var binding: FragmentPayBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerData()
    }

    private fun observerData() {
        viewModel.payData.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = PayAdapter(it)
        }
    }
}