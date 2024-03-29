package com.hn_2452.shoes_nike.ui.notification

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hn_2452.shoes_nike.data.model.Notification
import com.hn_2452.shoes_nike.databinding.LayoutNotificationItemBinding
import javax.inject.Inject

val NotificationUtil = object : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification) =
        oldItem._id == newItem._id

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification) =
        oldItem == newItem

}

class NotificationItemApdapter @Inject constructor(
) : ListAdapter<Notification, NotificationItemViewHolder>(NotificationUtil) {
    var mOnSelect: (Notification) -> Unit = { }
    lateinit var mNotificationViewModel: NotificationViewModel
    lateinit var viewLifecycleOwner: LifecycleOwner


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NotificationItemViewHolder(
            LayoutNotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            mOnSelect, mNotificationViewModel, viewLifecycleOwner

        )

    override fun onBindViewHolder(holder: NotificationItemViewHolder, position: Int) =
        holder.bind(getItem(position))

}

class NotificationItemViewHolder(
    private val mBinding: LayoutNotificationItemBinding,
    private val mOnSelect: (Notification) -> Unit,
    private val mViewModel: NotificationViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : ViewHolder(mBinding.root) {

    companion object {
        const val TAG = "Nike: NotificationItemViewHolder"
    }

    fun bind(notification: Notification) {
        Log.i(TAG, "bind ID : ${notification._id} ${notification.content} ${notification.seen}")
        mBinding.tvTitle.text = notification.title
        mBinding.tvContent.text = notification.content
        mBinding.root.setOnClickListener {
            mOnSelect(notification)
        }
        if(!notification.seen) {
            mViewModel.updateSeenNotification(notification._id).observe(viewLifecycleOwner) {
                if (it.data == true) {
                    Log.i(TAG, "update Seen ${notification.content} successfully")
                }
            }
        }
    }
}