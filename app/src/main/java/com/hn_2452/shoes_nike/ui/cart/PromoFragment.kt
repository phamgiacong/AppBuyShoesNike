package com.hn_2452.shoes_nike.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.Promo
import com.hn_2452.shoes_nike.adapter.PromoAdapter
import com.hn_2452.shoes_nike.viewModel.PromoViewModel
import com.hn_2452.shoes_nike.databinding.FragmentListPromoBinding
import com.hn_2452.shoes_nike.ultils.Status

class PromoFragment :BaseFragment<FragmentListPromoBinding>(){
    private var _mBinding: FragmentListPromoBinding? = null
    protected val  binding  get() = _mBinding!!
    private val promoViewModel : PromoViewModel by lazy{
        ViewModelProvider(this,
            PromoViewModel.PromoViewModelFactory(requireActivity().application))[
                    PromoViewModel::class.java
        ]
    }
    private val promoAdapter: PromoAdapter by lazy {
        PromoAdapter(onClick)
    }
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?)=
        FragmentListPromoBinding.inflate(inflater,container,false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mBinding= getViewBinding(inflater,container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvPromo.adapter = promoAdapter
        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }
        refreshData()
        binding.btnApply.setOnClickListener({
            var promo:Promo? = promoAdapter.getSelected()
            var b = Bundle().apply { putSerializable("promo",promo) }
            findNavController().navigate(R.id.confirmCartFragment,b)
        })
    }

    private fun refreshData() {
        promoViewModel.getPromo().observe(viewLifecycleOwner,{
            it?.let{resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        binding.swipeRefresh.isRefreshing = false
                        resoucce.data?.let{
                                promos ->  promoAdapter.setPromo(promos)
                        }
                    }
                    Status.ERROR ->{
                        binding.swipeRefresh.isRefreshing = false
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING ->{
                        binding.swipeRefresh.isRefreshing= true
                    }

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private var onClick:(Promo)  ->Unit ={
        binding.rcvPromo.post(Runnable {
            promoAdapter.notifyDataSetChanged()
        })

    }

}