package com.hn_2452.shoes_nike.ui.orders.e_wallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentMyEWalletBinding

class MyEWalletFragment : Fragment() {

    private val viewModel : MyEWalletViewModel by viewModels()
    private lateinit var binding: FragmentMyEWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyEWalletBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        init()
    }

    private fun init() {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        bindView()
//        onClickListener()
        observer()
    }

    private fun bindView() {
        TODO("Not yet implemented")
    }

    private fun onClickListener() {
        TODO("Not yet implemented")
    }

    private fun observer() {
        viewModel.eWalletData.observe(viewLifecycleOwner){
            binding.recyclerView.adapter = MyEWalletAdapter(it)
            Log.d("observer111111", "$it")
        }
    }

}