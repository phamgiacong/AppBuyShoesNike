package com.hn_2452.shoes_nike.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.Cart
import com.hn_2452.shoes_nike.viewModel.CartViewModel
import com.hn_2452.shoes_nike.data.Shipping
import com.hn_2452.shoes_nike.adapter.ShippingAdapter
import com.hn_2452.shoes_nike.viewModel.ShippingViewModel
import com.hn_2452.shoes_nike.databinding.FragmentChooseShippingBinding
import com.hn_2452.shoes_nike.ultils.Status

class ChooseShippingFragment :BaseFragment<FragmentChooseShippingBinding>() {

     override var _mBinding: FragmentChooseShippingBinding? = null
    val args by navArgs<ChooseShippingFragmentArgs>()
    protected val  binding  get() = _mBinding!!
    private val shippingViewModel : ShippingViewModel by lazy {
        ViewModelProvider(this,
            ShippingViewModel.ShippingViewModelFactory(requireActivity().application))[
            ShippingViewModel::class.java
        ]
    }
    private val  shippingAdapter: ShippingAdapter by lazy {
        ShippingAdapter(onClick)
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(this, CartViewModel.CartViewModelFactory(requireActivity().application))[
            CartViewModel::class.java
        ]
    }
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChooseShippingBinding.inflate(inflater,container,false)

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mBinding = getViewBinding(inflater,container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgBack.setOnClickListener({
            findNavController().navigate(R.id.cartFragment)
        })
        binding.rcvShipping.adapter = shippingAdapter
        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }

        refreshData()
        binding.btnApply.setOnClickListener({
            var shipping :Shipping? = shippingAdapter.getSelected()
            var cart = Cart(null,"",null,"","",shipping?._id,null)
            updateShipping(args.idCart,cart,shipping)

        })
    }
    private fun refreshData(){
        shippingViewModel.getShipping().observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        binding.swipeRefresh.isRefreshing = false
                        resoucce.data?.let{
                            shippings ->  shippingAdapter.setShippings(shippings)
                        }
                    }
                    Status.ERROR ->{
                        binding.swipeRefresh.isRefreshing = false
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING ->{
                        binding.swipeRefresh.isRefreshing= true
                    }

                }
            }
        })
    }
    private var onClick:(Shipping)  ->Unit ={
        binding.rcvShipping.post(Runnable {
            shippingAdapter.notifyDataSetChanged()
        })

    }
    private fun updateShipping(id:String, cart: Cart, shipping: Shipping?){
        cartViewModel.updateShipping(id,cart).observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        var b = Bundle().apply { putParcelable("shipping",shipping) }
                        findNavController().navigate(R.id.confirmCartFragment,b)
                    }
                    Status.ERROR ->{
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()

                    }
                    Status.LOADING ->{

                    }

                }
            }
        })
    }
}