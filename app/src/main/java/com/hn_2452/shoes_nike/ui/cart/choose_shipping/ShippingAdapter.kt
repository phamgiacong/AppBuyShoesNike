package com.hn_2452.shoes_nike.ui.cart.choose_shipping

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.data.model.Shipping
import com.hn_2452.shoes_nike.databinding.ItemShippingBinding
import java.text.DateFormat
import java.util.Calendar

class ShippingAdapter (
    private var onClick :(Shipping) ->Unit
): RecyclerView.Adapter<ShippingAdapter.ViewHolder>() {
    private var listShipping :List<Shipping> = listOf()
    private var selected :Int =-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemShippingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBindding.tvNameShipping.text = listShipping.get(position).name
        holder.itemBindding.tvPrice.text = "$"+listShipping.get(position).price
        holder.itemBindding.radio.isChecked = selected==position
        holder.itemBindding.radio.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                selected = holder.adapterPosition
                onClick(listShipping.get(selected))
            }
        })
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        calendar.add(Calendar.DAY_OF_MONTH,listShipping.get(position).days)
        val  receivedDay= calendar.get(Calendar.DAY_OF_MONTH)
        var receivedMonth = calendar.get(Calendar.MONTH)
        holder.itemBindding.tvTime.text="Estimated arrival,$today/$month - $receivedDay/$receivedMonth"

    }
    public fun getSelected(): Shipping? {
        if(selected ==-1){
            return null
        }
        return listShipping.get(selected)
    }

    override fun getItemCount(): Int {
        return listShipping.size
    }
    inner class ViewHolder(var itemBindding : ItemShippingBinding) : RecyclerView.ViewHolder(itemBindding.root) {
    }
    fun setShippings(listShipping :List<Shipping>){
        this.listShipping = listShipping
        notifyDataSetChanged()
    }


}