package com.hn_2452.shoes_nike.ui.cart.shipment_address

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Address
import com.hn_2452.shoes_nike.data.model.Cart
import com.hn_2452.shoes_nike.ui.cart.CartViewModel
import com.hn_2452.shoes_nike.databinding.FragmentListAddressBinding
import com.hn_2452.shoes_nike.ui.cart.AddressViewModel
import com.hn_2452.shoes_nike.utility.Status

class ShippingAddressFragment: BaseFragment<FragmentListAddressBinding>() {
    private var _mBinding: FragmentListAddressBinding? = null
    val args by navArgs<ShippingAddressFragmentArgs>()
    protected val  binding  get() = _mBinding!!
    private val addressViewModel: AddressViewModel by lazy {
        ViewModelProvider(
            this,
            AddressViewModel.AddressViewModelFactory(requireActivity().application))[
                    AddressViewModel::class.java
        ]
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(this, CartViewModel.CartViewModelFactory(requireActivity().application))[
            CartViewModel::class.java
        ]
    }
    private val addressAdapter: AddressAdapter by lazy {
        AddressAdapter( onClick )
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentListAddressBinding.inflate(inflater,container,false)

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
            findNavController().navigate(R.id.confirmCartFragment)
        })
        binding.rcvAddress.adapter =addressAdapter
        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }
        refreshData()
        binding.btnApply.setOnClickListener({
            var address: Address? = addressAdapter.getSelected()
            var cart = Cart(null,"",null,address?._id,"",0.0,null)
            updateAddressToCart(args.idCart,cart)

        })
    }

    private fun refreshData() {
        addressViewModel.getAddressByIdU("123").observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        binding.swipeRefresh.isRefreshing = false
                        resoucce.data?.let{
                              addresses ->  addressAdapter.setAddress(addresses)
                        }
                    }
                    Status.ERROR ->{
                        binding.swipeRefresh.isRefreshing = false
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        Log.e("TAG", "refreshData: ${it.message}", )
                    }
                    Status.LOADING ->{
                        binding.swipeRefresh.isRefreshing= true
                    }

                }
            }
        })
    }

    private var onClick:(Address)  ->Unit ={
        binding.rcvAddress.post(Runnable {
            addressAdapter.notifyDataSetChanged()
        })

    }
    private fun updateAddressToCart(id:String, cart: Cart){
        cartViewModel.updateAddress(id,cart).observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        findNavController().navigate(R.id.confirmCartFragment)
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