package com.hn_2452.shoes_nike.ui.cart

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.model.ShoesToCart
import com.hn_2452.shoes_nike.databinding.ItemCartBinding
import com.hn_2452.shoes_nike.utility.Status

class ShoesToCartAdapter(
    private var onClick:(ShoesToCart, Shoes) ->Unit,
    private val shoesViewModel: ShoesViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val shoesToCartViewModel: ShoesToCartViewModel,


    ): RecyclerView.Adapter<ShoesToCartAdapter.ViewHolder>() {
    var totalPrice:Double =0.0
    private var listShoesToCart :List<ShoesToCart> = listOf()
    inner class ViewHolder(var itemBindding:ItemCartBinding): RecyclerView.ViewHolder(itemBindding.root){
        fun bind(shoes: Shoes, shoesToCart: ShoesToCart){
            itemBindding.apply {
                tvNameProduct.text = shoes.name
                tvPriceProduct.text = "$${shoes.price*shoesToCart.quantity}"
                editQuantity.text="${shoesToCart.quantity}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCartBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return  listShoesToCart.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var shoesToCart: ShoesToCart = listShoesToCart.get(position)
        // tăng giảm số lượng sản phẩm
        var quantity:Int = shoesToCart.quantity
        holder.itemBindding.btnAugment.setOnClickListener({
            quantity++
            var shoes: ShoesToCart = ShoesToCart(shoesToCart._id,shoesToCart.idU,shoesToCart.idShoes,shoesToCart.colorChoose,shoesToCart.sizeChoose,shoesToCart.createdDate,quantity)
            UpdateQuantity(shoes,holder)
        })
        holder.itemBindding.btnReduce.setOnClickListener({
            quantity--
            var shoes: ShoesToCart = ShoesToCart(shoesToCart._id,shoesToCart.idU,shoesToCart.idShoes,shoesToCart.colorChoose,shoesToCart.sizeChoose,shoesToCart.createdDate,quantity)
            UpdateQuantity(shoes,holder)
        })
        holder.itemBindding.editQuantity.text = "${shoesToCart.quantity}"
        holder.itemBindding.tvSize.text ="Size = ${shoesToCart.sizeChoose}"
        holder.itemBindding.cavColor.setCardBackgroundColor(Color.parseColor("${shoesToCart.colorChoose}"))
        getShoes(holder,shoesToCart)
        getTotalPrice()
    }
    fun setShoesToCart(listShoesToCart:List<ShoesToCart>){
        this.listShoesToCart =listShoesToCart
        notifyDataSetChanged()

    }
    private fun getTotalPrice(){
        var totalPrice :Double =0.0
        shoesToCartViewModel.getShoesToCartByIdU("123").observe(viewLifecycleOwner,{
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS ->{
                        resource.data?.let {
                                shoesToCarts ->  for(i in shoesToCarts){
                            shoesViewModel.getShoesById(i.idShoes).observe(viewLifecycleOwner,{
                                it?.let { resource ->
                                    when(resource.status){
                                        Status.SUCCESS->{
                                            resource.data?.let {
                                                    shoes -> totalPrice = totalPrice + (shoes.price* i.quantity)
                                                shoesViewModel.getTotalPrice.value = totalPrice
                                            }
                                        }Status.ERROR ->{
                                    }Status.LOADING ->{
                                    }
                                    }
                                }
                            })
                        }
                        }
                    }Status.LOADING ->{
                }Status.ERROR ->{
                }
                }
            }
        })
    }
    fun getShoes(holder: ViewHolder, shoesToCart: ShoesToCart){
        shoesViewModel.getShoesById(shoesToCart.idShoes).observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        resoucce.data?.let {
                                shoes -> holder.bind(shoes,shoesToCart)
                            getTotalPrice()
                            holder.itemBindding.removeItem.setOnClickListener({
                                onClick(shoesToCart,shoes)
                            })
                        }
                    }
                    Status.ERROR ->{
                        Log.e("TAG", "refreshData: ${it.message}", )
                    }
                    Status.LOADING ->{
                    }
                }
            }
        })
    }
    fun UpdateQuantity(shoesToCart: ShoesToCart, holder: ViewHolder){
        shoesToCartViewModel.updateShoesToCartById(shoesToCart._id,shoesToCart).observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS->{
                        resoucce.data?.let {
                                shoesToCart ->
                            Log.e("TAG", "UpdateQuantity: $shoesToCart" )
                            getShoes(holder,shoesToCart)
                            getTotalPrice()
                        }
                    }
                    Status.LOADING->{

                    }
                    Status.ERROR ->{
                        Log.e("TAG", "UpdateQuantity: ${it.message}", )
                    }
                }
            }
        })
    }


}