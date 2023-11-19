package com.hn_2452.shoes_nike.ui.cart

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
import com.hn_2452.shoes_nike.data.Address
import com.hn_2452.shoes_nike.viewModel.AddressViewModel
import com.hn_2452.shoes_nike.data.Cart
import com.hn_2452.shoes_nike.viewModel.CartViewModel
import com.hn_2452.shoes_nike.adapter.ConfirmShoesToCartApdater
import com.hn_2452.shoes_nike.viewModel.ShippingViewModel
import com.hn_2452.shoes_nike.data.Shoes
import com.hn_2452.shoes_nike.data.ShoesToCart
import com.hn_2452.shoes_nike.viewModel.ShoesToCartViewModel
import com.hn_2452.shoes_nike.viewModel.ShoesViewModel
import com.hn_2452.shoes_nike.databinding.FragmentConfirmCartBinding
import com.hn_2452.shoes_nike.ultils.Status

class ConfirmCartFragment:BaseFragment<FragmentConfirmCartBinding>() {
    override var _mBinding: FragmentConfirmCartBinding? = null
    protected val  binding  get() = _mBinding!!
    private var cart =Cart()
    val args by navArgs<ConfirmCartFragmentArgs>()
    private var discount:Double =0.0;
    private val shoesToCartViewModel: ShoesToCartViewModel by lazy{
        ViewModelProvider(this, ShoesToCartViewModel.ShoesToCartViewModelFactory(requireActivity().application))[
            ShoesToCartViewModel::class.java
        ]
    }
    private val shippingViewModel : ShippingViewModel by lazy {
        ViewModelProvider(this,
            ShippingViewModel.ShippingViewModelFactory(requireActivity().application))[
            ShippingViewModel::class.java
        ]
    }
    private val confirmShoesToCartApdater: ConfirmShoesToCartApdater by lazy {
        ConfirmShoesToCartApdater(shoesViewModel,viewLifecycleOwner)
    }
    private val shoesViewModel: ShoesViewModel by lazy {
        ViewModelProvider(this, ShoesViewModel.ShoesViewModelFactory(requireActivity().application))[
            ShoesViewModel::class.java
        ]
    }
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
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentConfirmCartBinding.inflate(inflater,container,false)

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
        var promo = args.promo

        if(promo!=null){
           discount=promo.discount
        }
        binding.rcvShoesToCart.adapter=confirmShoesToCartApdater
        getShoesToCart()
        getIdCart("123")
        binding.btnEditAddress.setOnClickListener({
            var b = Bundle().apply { putString("idCart",cart._id)}
            findNavController().navigate(R.id.shippingAddressFragment,b)
        })
        binding.btnChooseShipping.setOnClickListener({
            var b = Bundle().apply { putString("idCart",cart._id)}
            findNavController().navigate(R.id.chooseShippingFragment,b)
        })
        binding.btnAddPromo.setOnClickListener({
            findNavController().navigate(R.id.promoFragment)
        })
        binding.btnEditShipping.setOnClickListener({
            var b = Bundle().apply { putString("idCart",cart._id)}
            findNavController().navigate(R.id.chooseShippingFragment,b)
        })
        binding.btnChooseAddress.setOnClickListener({
            findNavController().navigate(R.id.shippingAddressFragment)
        })
    }
    private fun getDefaultAddress (){
        addressViewModel.getAddressByIdU("123").observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        resoucce.data?.let {
                            addresses -> filterAddress(addresses)
                        }
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
    private fun getShoesToCart(){
        shoesToCartViewModel.getShoesToCartByIdU("123").observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        resoucce.data?.let {
                                shoesToCarts ->  confirmShoesToCartApdater.setShoesToCart(shoesToCarts)
                            getTotalPrice(shoesToCarts)
                        }
                    }
                    Status.ERROR ->{

                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING->{
                        Log.e("TAG", "onViewCreated:4 ", )
                    }
                }
            }
        })
    }
    private fun filterAddress(addresses: List<Address>){
        if(addresses.size == 0){
            binding.cavInfoAddress.visibility=View.GONE
            binding.cavChooseAddress.visibility=View.VISIBLE
        }
        for(i in addresses){
            if(i.default==true){
                binding.tvNameAddress.text=i.name
                binding.tvAddress.text =i.address
                binding.defaulted.visibility = View.VISIBLE
            }
        }
    }
    private fun getIdCart(idU:String){
        cartViewModel.getCart(idU).observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        resoucce.data?.let {
                         carts ->
                            cart = carts.get(0)
                            getAddress(cart.idAddress)
                            getShippingById(cart.idShipping)
                        }
                    }
                    Status.ERROR ->{
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING->{

                    }
                }
            }
        })
    }
    private fun getTotalPrice(list:List<ShoesToCart>){
        var totalPrice :Double =0.0
        for(i in list){
            shoesViewModel.getShoesById(i.idShoes).observe(viewLifecycleOwner,{
                it?.let { resoucce ->
                    when(resoucce.status){
                        Status.SUCCESS ->{
                            resoucce.data?.let { shoes -> totalPrice = totalPrice+(shoes.price*i.quantity)
                                    binding.tvAmount.text="$$totalPrice"
                                    if(discount>0){
                                        binding.layoutPromo.visibility=View.VISIBLE
                                        binding.tvDiscount.text="-$${totalPrice*discount}"
                                    }
                                Log.e("TAG", "getTotalPrice: $totalPrice " )
                            }
                        }
                        Status.ERROR->{
                        }
                        Status.LOADING->{
                        }
                    }
                }
            })
        }

    }
    private var onClick:(ShoesToCart, Shoes) ->Unit= { shoesToCart: ShoesToCart, shoes: Shoes ->

    }
    private fun getAddress(id:String?){
        if(id!=null){
            addressViewModel.getAddressById(id).observe(viewLifecycleOwner,{
                it?.let { resoucce ->
                    when(resoucce.status){
                        Status.SUCCESS ->{
                            resoucce.data?.let {
                                    address ->
                                binding.tvNameAddress.text = address.name
                                binding.tvAddress.text = address.address
                                if(address.default==true) {
                                    binding.defaulted.visibility = View.VISIBLE
                                }
                            }
                        }
                        Status.ERROR ->{
                            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING->{
                            Log.e("TAG", "onViewCreated:4 ", )
                        }
                    }
                }
            })
        }else{
            getDefaultAddress()
        }
    }
    private fun getShippingById(id:String?){
        if(id!=null){
            shippingViewModel.getShippingById(id).observe(viewLifecycleOwner,{
                it?.let { resoucce ->
                    when(resoucce.status){
                        Status.SUCCESS ->{
                            resoucce.data?.let {
                                shipping ->
                                binding.cavChooseShipping.visibility =View.GONE
                                binding.cavItemShipping.visibility=View.VISIBLE
                                binding.tvNameShipping.text=shipping.name
                                binding.tvPriceShipping.text="$${shipping.price}"
                                binding.priceShipping.text="$${shipping.price}"
                            }
                        }
                        Status.ERROR ->{
                            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING->{
                            Log.e("TAG", "onViewCreated:4 ", )
                        }
                    }
                }
            })
        }
    }

}