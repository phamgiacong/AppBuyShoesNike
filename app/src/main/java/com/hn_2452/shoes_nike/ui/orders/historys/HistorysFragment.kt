package com.hn_2452.shoes_nike.ui.orders.historys

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentHistorysBinding
import com.hn_2452.shoes_nike.ui.orders.e_wallet.MyEWalletAdapter
import com.hn_2452.shoes_nike.ui.orders.e_wallet.MyEWalletViewModel

class HistorysFragment : Fragment() {

    private lateinit var binding: FragmentHistorysBinding
    private val viewModel: MyEWalletViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistorysBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerData()
    }

    private fun observerData() {
        viewModel.eWalletData.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = MyEWalletAdapter(it)
        }
    }
}