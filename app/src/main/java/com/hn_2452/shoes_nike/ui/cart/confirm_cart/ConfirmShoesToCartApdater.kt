package com.hn_2452.shoes_nike.ui.cart.confirm_cart

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.data.model.ShoesToCart
import com.hn_2452.shoes_nike.ui.cart.ShoesViewModel
import com.hn_2452.shoes_nike.databinding.ItemCartBinding
import com.hn_2452.shoes_nike.utility.Status

class ConfirmShoesToCartApdater(
    private val shoesViewModel: ShoesViewModel,
    private val viewLifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<ConfirmShoesToCartApdater.ViewHolder>() {
    private var listShoesToCart :List<ShoesToCart> = listOf()

    inner class ViewHolder(var itemBindding:ItemCartBinding): RecyclerView.ViewHolder(itemBindding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCartBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return  listShoesToCart.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var shoesToCart: ShoesToCart = listShoesToCart.get(position)
        holder.itemBindding.cavEditQuantity.visibility= View.GONE
        holder.itemBindding.cavQuantity.visibility=View.VISIBLE
        holder.itemBindding.removeItem.visibility=View.GONE
        holder.itemBindding.tvSize.text ="Size = ${shoesToCart.sizeChoose}"
        holder.itemBindding.cavColor.setCardBackgroundColor(Color.parseColor("${shoesToCart.colorChoose}"))
        holder.itemBindding.tvQuantity.text="${shoesToCart.quantity}"
        getShoes(holder,shoesToCart)
    }
    fun getShoes(holder: ViewHolder, shoesToCart: ShoesToCart){
        shoesViewModel.getShoesById(shoesToCart.idShoes).observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        resoucce.data?.let {
                               shoes ->  holder.itemBindding.tvNameProduct.text=shoes.name
                                holder.itemBindding.tvPriceProduct.text="$${shoes.price*shoesToCart.quantity}"
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
    fun setShoesToCart(listShoesToCart:List<ShoesToCart>){
        this.listShoesToCart =listShoesToCart
        notifyDataSetChanged()

    }

}