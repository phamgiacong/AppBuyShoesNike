package com.hn_2452.shoes_nike.ui.cart.confirm_cart

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
import com.hn_2452.shoes_nike.ui.cart.AddressViewModel
import com.hn_2452.shoes_nike.data.model.Cart
import com.hn_2452.shoes_nike.data.model.Shipping
import com.hn_2452.shoes_nike.ui.cart.CartViewModel
import com.hn_2452.shoes_nike.ui.cart.ShippingViewModel
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.model.ShoesToCart
import com.hn_2452.shoes_nike.ui.cart.ShoesToCartViewModel
import com.hn_2452.shoes_nike.databinding.FragmentConfirmCartBinding
import com.hn_2452.shoes_nike.ui.cart.ShoesViewModel
import com.hn_2452.shoes_nike.utility.Status
import java.util.Calendar

class ConfirmCartFragment:BaseFragment<FragmentConfirmCartBinding>() {
    private var _mBinding: FragmentConfirmCartBinding? = null
    protected val  binding  get() = _mBinding!!
    private var cart1 = Cart()
    val args by navArgs<ConfirmCartFragmentArgs>()
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
        binding.rcvShoesToCart.adapter=confirmShoesToCartApdater
        getShoesToCart()
        getIdCart("123")
        binding.btnEditAddress.setOnClickListener({
            var b = Bundle().apply { putString("idCart",cart1._id)}
            findNavController().navigate(R.id.shippingAddressFragment,b)
        })
        binding.btnChooseShipping.setOnClickListener({
            var b = Bundle().apply { putString("idCart",cart1._id)}
            findNavController().navigate(R.id.chooseShippingFragment,b)
        })
        binding.btnEditShipping.setOnClickListener({
            var b = Bundle().apply { putString("idCart",cart1._id)}
            findNavController().navigate(R.id.chooseShippingFragment,b)
        })
        binding.btnChooseAddress.setOnClickListener({
            findNavController().navigate(R.id.shippingAddressFragment)
        })
        binding.btnContinueToPay.setOnClickListener({
            validateCart("123")
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
                var cart = Cart(null,"",null,i?._id,"",0.0,null)
                cart1._id?.let { updateAddressToCart(it,cart) }
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
                                cart ->
                            cart1 =cart
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
                                shoesViewModel.getTotalPrice.value = totalPrice
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
                            getDefaultAddress()
                        }
                        Status.LOADING->{
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
                                val calendar = Calendar.getInstance()
                                val today = calendar.get(Calendar.DAY_OF_MONTH)
                                val month = calendar.get(Calendar.MONTH)
                                calendar.add(Calendar.DAY_OF_MONTH,shipping.days)
                                val  receivedDay= calendar.get(Calendar.DAY_OF_MONTH)
                                var receivedMonth = calendar.get(Calendar.MONTH)
                                binding.tvTimeShipping.text ="Estimated arrival,$today/$month - $receivedDay/$receivedMonth"
                                shoesViewModel.getTotalPrice.observe(viewLifecycleOwner,{
                                    binding.tvTotal.text ="$${it+shipping.price!!}"
                                    var cart = Cart(null,"",null,"","",it+shipping.price!!,null)
                                    cart1._id?.let { it1 -> cartViewModel.udapteTotalPrice(it1,cart).observe(viewLifecycleOwner,{
                                        Log.e("TAG", "update total price ", )
                                    }) }
                                })
                            }
                        }
                        Status.ERROR ->{
                        }
                        Status.LOADING->{
                        }
                    }
                }
            })
        }
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
    private fun validateCart(idU:String){
        cartViewModel.getCart(idU).observe(viewLifecycleOwner,{
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS ->{
                        resource.data?.let { cart ->
                            if(!cart.idShipping.equals("")&&!cart.idAddress.equals("")){
                                findNavController().navigate(R.id.paymentFragment )
                            }else{
                                Toast.makeText(requireContext(),"Information is missing",Toast.LENGTH_LONG).show()
                            }
                        }
                    }Status.LOADING ->{

                }
                    Status.ERROR ->{

                    }
                }
            }
        }
        )
    }

}