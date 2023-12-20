package com.hn_2452.shoes_nike.ui.cart.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.data.model.Address
import com.hn_2452.shoes_nike.databinding.ItemAddressBinding
import javax.inject.Inject

class AddressAdapterViewHolder(
    private val mBinding: ItemAddressBinding,
    private val mOnSelect: (Address) -> Unit,
    private val mOnEdit: (Address) -> Unit
) : RecyclerView.ViewHolder(mBinding.root) {

    fun bind(address: Address) {
        mBinding.tvAddress.text = address.address
        mBinding.tvNameAddress.text = address.name
        mBinding.tvPhoneNumber.text = address.phoneNumber

        var more = if(address.type == 0) { "Nhà riêng" } else { "Văn phòng"}
        if(address.default) {
            more += " - Địa chỉ mặc định"
        }
        mBinding.tvMore.text = more

        mBinding.root.setOnClickListener {
            mOnSelect(address)
        }

        mBinding.imvEdit.setOnClickListener {
            mOnEdit(address)
        }
    }

}

val AddressDiff = object : DiffUtil.ItemCallback<Address>() {
    override fun areItemsTheSame(oldItem: Address, newItem: Address) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean = oldItem == newItem
}

class AddressAdapter @Inject constructor() :
    ListAdapter<Address, AddressAdapterViewHolder>(AddressDiff) {

    var mOnSelect: (Address) -> Unit = {}
    var mOnEdit: (Address) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AddressAdapterViewHolder(
            ItemAddressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            mOnSelect,
            mOnEdit
        )

    override fun onBindViewHolder(holder: AddressAdapterViewHolder, position: Int) =
        holder.bind(getItem(position))

}