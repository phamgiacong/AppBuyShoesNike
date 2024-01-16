package com.hn_2452.shoes_nike.ui.notification

import com.airbnb.epoxy.TypedEpoxyController
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Notification
import com.hn_2452.shoes_nike.data.model.Order
import com.hn_2452.shoes_nike.databinding.LayoutNotificationItemBinding
import com.hn_2452.shoes_nike.utility.ViewBindingModel

class NotificationAdapterController(val mOnDetail: (String) -> Unit) : TypedEpoxyController<List<Notification>>() {
    override fun buildModels(data: List<Notification>?) {
        data?.forEach { notification ->
            NotificationModel(notification,mOnDetail).id(notification._id).addTo(this)
        }
    }

    class NotificationModel(
        private val mNotification: Notification,
        private val mOnDetail: (String) -> Unit
    ) : ViewBindingModel<LayoutNotificationItemBinding>(R.layout.layout_notification_item) {
        override fun LayoutNotificationItemBinding.bind() {
            tvTitle.text = mNotification.title
            tvContent.text = mNotification.content
            layoutNotification.setOnClickListener({
                mNotification.id_user?.let { it1 -> mOnDetail(it1) }
            })
        }

    }

}