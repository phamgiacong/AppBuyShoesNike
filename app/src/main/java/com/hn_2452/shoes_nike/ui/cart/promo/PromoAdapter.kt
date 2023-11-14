package com.hn_2452.shoes_nike.ui.cart.promo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.data.model.Promo
import com.hn_2452.shoes_nike.databinding.ItemPromoBinding

class PromoAdapter(
    private var onClick:(Promo) ->Unit
): RecyclerView.Adapter<PromoAdapter.ViewHolder>() {
    private var listPromo:List<Promo> = listOf()
    private var selected :Int =-1

    inner class ViewHolder(var itemBindding : ItemPromoBinding) : RecyclerView.ViewHolder(itemBindding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPromoBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return  listPromo.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBindding.tvName.text = listPromo.get(position).name
        holder.itemBindding.tvTitle.text = listPromo.get(position).title
        holder.itemBindding.radio.isChecked = selected==position
        holder.itemBindding.radio.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                selected =holder.adapterPosition
                onClick(listPromo.get(selected))
            }
        })
    }
    public fun getSelected(): Promo?{
        if(selected==-1){
            return null
        }
        return listPromo.get(selected)
    }
    fun setPromo(listPromo:List<Promo>){
        this.listPromo =listPromo
        notifyDataSetChanged()

    }
}