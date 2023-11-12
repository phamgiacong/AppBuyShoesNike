package com.hn_2452.shoes_nike.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.data.Shoes
import com.hn_2452.shoes_nike.data.ShoesToCart
import com.hn_2452.shoes_nike.viewModel.ShoesToCartViewModel
import com.hn_2452.shoes_nike.viewModel.ShoesViewModel
import com.hn_2452.shoes_nike.databinding.ItemCartBinding
import com.hn_2452.shoes_nike.ultils.Status

class ShoesToCartAdapter(
    private var onClick:(ShoesToCart, Shoes) ->Unit,
    private val shoesViewModel: ShoesViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val shoesToCartViewModel: ShoesToCartViewModel,


    ): RecyclerView.Adapter<ShoesToCartAdapter.ViewHolder>() {
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
    }
    fun setShoesToCart(listShoesToCart:List<ShoesToCart>){
        this.listShoesToCart =listShoesToCart
        notifyDataSetChanged()

    }
    fun getShoes(holder: ViewHolder, shoesToCart: ShoesToCart){
        shoesViewModel.getShoesById(shoesToCart.idShoes).observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        resoucce.data?.let {
                                shoes -> holder.bind(shoes,shoesToCart)
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