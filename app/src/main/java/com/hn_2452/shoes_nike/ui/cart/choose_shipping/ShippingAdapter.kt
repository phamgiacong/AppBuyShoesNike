package com.hn_2452.shoes_nike.ui.cart.choose_shipping

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
        var calendar = Calendar.getInstance().time

        var dateFormat= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar)
        holder.itemBindding.tvTime.text ="Estimated Arrival: ${dateFormat.substring(0,2)}"
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