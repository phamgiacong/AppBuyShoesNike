package com.hn_2452.shoes_nike.ui.orders.topup.topupprice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.TopUpModel

class TopupAdapter(private val list: List<TopUpModel>) :
    RecyclerView.Adapter<TopupAdapter.TopupViewHolder>() {

    inner class TopupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val price: TextView = view.findViewById(R.id.price)
        fun bindView(index: Int) {
            val item = list[index]
            price.text = item.price
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupViewHolder {
        return TopupViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_topup, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TopupViewHolder, position: Int) {
        holder.bindView(position)
    }
}