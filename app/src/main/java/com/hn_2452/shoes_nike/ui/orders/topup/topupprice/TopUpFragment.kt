package com.hn_2452.shoes_nike.ui.orders.topup.topupprice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.databinding.FragmentTopUpBinding

class TopUpFragment : Fragment() {

    private val viewModel: TopupViewModel by viewModels()
    private lateinit var binding: FragmentTopUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerData()
    }

    private fun observerData() {
        viewModel.topupData.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = TopupAdapter(it)
        }
    }

}