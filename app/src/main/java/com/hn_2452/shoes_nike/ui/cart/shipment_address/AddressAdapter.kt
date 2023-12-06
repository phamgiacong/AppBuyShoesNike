package com.hn_2452.shoes_nike.ui.cart.shipment_address

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.data.model.Address
import com.hn_2452.shoes_nike.databinding.ItemAddressBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar

class AddressAdapter(
    private var onClick:(Address) ->Unit
):RecyclerView.Adapter<AddressAdapter.ViewHolder>(){
    private var listAddress :List<Address> = listOf()
    private var selected :Int =-1

    inner class ViewHolder(var itemBindding :ItemAddressBinding): RecyclerView.ViewHolder(itemBindding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return listAddress.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBindding.tvNameAddress.text = listAddress.get(position).name
        holder.itemBindding.tvAddress.text = listAddress.get(position).address
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val current = formatter.format(time)
        Log.e("TAG", "onBindViewHolder: $current", )
        if(listAddress.get(position).default==true){
            holder.itemBindding.defaulted.visibility = View.VISIBLE
        }
        holder.itemBindding.radiobtn.isChecked = selected==position
        holder.itemBindding.radiobtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                selected =holder.adapterPosition
                onClick(listAddress.get(selected))
            }
        }
    }

    public fun getSelected(): Address?{
        if(selected==-1){
            return null
        }
        return listAddress.get(selected)
    }
    fun setAddress(listAddress:List<Address>){
        this.listAddress =listAddress
        notifyDataSetChanged()

    }
}