package com.hn_2452.shoes_nike.ui.cart

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Cart
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.model.ShoesToCart
import com.hn_2452.shoes_nike.databinding.FragmentBottomBinding
import com.hn_2452.shoes_nike.databinding.FragmentMyCartBinding
import com.hn_2452.shoes_nike.utility.Status

class CartFragment : BaseFragment<FragmentMyCartBinding>() {
    private var _mBinding: FragmentMyCartBinding? = null
    protected val  binding  get() = _mBinding!!
    private val    listIdShoesToCart=ArrayList<String>()
    private val shoesToCartViewModel: ShoesToCartViewModel by lazy{
        ViewModelProvider(this, ShoesToCartViewModel.ShoesToCartViewModelFactory(requireActivity().application))[
                ShoesToCartViewModel::class.java
        ]
    }
    private val shoesViewModel: ShoesViewModel by lazy {
        ViewModelProvider(this, ShoesViewModel.ShoesViewModelFactory(requireActivity().application))[
                ShoesViewModel::class.java
        ]
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(this, CartViewModel.CartViewModelFactory(requireActivity().application))[
            CartViewModel::class.java
                ]
    }
    private val shoesToCartAdapter: ShoesToCartAdapter by lazy {
        ShoesToCartAdapter(onClick,shoesViewModel,viewLifecycleOwner,shoesToCartViewModel)
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMyCartBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mBinding = getViewBinding(inflater,container);
        return  binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCheckOut.setOnClickListener({
            findNavController().navigate(R.id.confirmCartFragment)
            var cart = Cart(null,"123",listIdShoesToCart,"","","",null)
            Log.e("TAG", "onViewCreated: $cart ", )
            createCart("123",cart)
        })
        binding.rcvShoes.adapter = shoesToCartAdapter
        binding.swipeRefresh.setOnRefreshListener {
            Log.e("TAG", "onViewCreated: ", )
            refreshData()
        }
        refreshData()

    }
    public fun refreshData(){
        shoesToCartViewModel.getShoesToCartByIdU("123").observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        binding.swipeRefresh.isRefreshing=false
                        resoucce.data?.let {
                            shoesToCarts ->  shoesToCartAdapter.setShoesToCart(shoesToCarts)
                                            getTotalPrice(shoesToCarts)
                                            for (i in shoesToCarts){
                                                Log.e("TAG", "refreshData:${i._id} ", )
                                                listIdShoesToCart.add(i._id)
                                            }
                        }
                    }
                    Status.ERROR ->{
                        binding.swipeRefresh.isRefreshing = false
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING->{
                        Log.e("TAG", "onViewCreated:4 ", )
                        binding.swipeRefresh.isRefreshing=true
                    }
                }
            }
        })
    }
    private var onClick:(ShoesToCart, Shoes) ->Unit={ shoesToCart: ShoesToCart, shoes: Shoes ->
        binding.rcvShoes.post({
            shoesToCartAdapter.notifyDataSetChanged()
        })
        var dialog:BottomSheetDialog = BottomSheetDialog(requireContext())
        var viewBidding:FragmentBottomBinding =FragmentBottomBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(viewBidding.root)
        viewBidding.tvNameProduct.text = shoes.name
        viewBidding.tvPriceProduct.text ="$${shoes.price*shoesToCart.quantity}"
        viewBidding.tvQuantity.text ="${shoesToCart.quantity}"
        viewBidding.cavColor.setCardBackgroundColor(Color.parseColor(shoesToCart.colorChoose))
        viewBidding.btnClose.setOnClickListener({
            dialog.dismiss()
        })
        viewBidding.btnRemove.setOnClickListener({
            shoesToCartViewModel.deleteShoesToCartById(shoesToCart._id).observe(viewLifecycleOwner,{
                it?.let { resoucce ->
                    when(resoucce.status){
                        Status.SUCCESS ->{
                            Toast.makeText(requireContext(),"Remove Success",Toast.LENGTH_LONG).show()
                            refreshData()
                            dialog.dismiss()
                        }
                        Status.ERROR ->{
                            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                            Log.e("TAG", "refreshData: ${it.message}", )
                        }
                        Status.LOADING->{
                        }
                    }
                }
            })
        })
        dialog.show()
    }
    private fun getTotalPrice(list:List<ShoesToCart>){
        var totalPrice :Double =0.0
        for(i in list){
            shoesViewModel.getShoesById(i.idShoes).observe(viewLifecycleOwner,{
                it?.let { resoucce ->
                    when(resoucce.status){
                        Status.SUCCESS ->{
                            resoucce.data?.let { shoes -> totalPrice = totalPrice+(shoes.price*i.quantity)
                                binding.tvPrice.text="$$totalPrice"
                            }
                        }
                        Status.ERROR->{
                            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                            Log.e("TAG", "refreshData: ${it.message}", )
                        }
                        Status.LOADING->{
                        }
                    }
                }
            })
        }

    }
    private fun createCart(idU:String,cart: Cart){
        cartViewModel.postCart(idU,cart).observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
                        Log.e("TAG", "createCart:ok ", )
                    }
                    Status.ERROR->{
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        Log.e("TAG", "refreshData: ${it.message}", )
                    }
                    Status.LOADING->{
                    }
                }
            }
        })
    }

}