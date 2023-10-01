package com.hn_2452.shoes_nike.data.model

class OrderModel private constructor(
    var id: String,

    var idUser: String,

    var nameProduct: String,
    var colorProduct: String,
    var sizeProduct: String,
    var quantity: Int,
    var imageProduct: String,

    var total: Int,

    var orderPlaced: Boolean,
    var dateOrderPlaced: String,

    var orderConfirmed: Boolean,
    var dateOrderConfirmed: String,

    var orderShipped: Boolean,
    var dateOrderShipped: String,

    var outForDelivery: Boolean,
    var dateOutForDelivery: String,

    var orderDelivered: Boolean,
    var dateOrderDelivered: String,

    var reviewed: Boolean

) {
    class Builder {
        private var id: String = ""
        private var idUser: String = ""
        private var nameProduct: String = ""
        private var colorProduct: String = ""
        private var sizeProduct: String = ""
        private var quantity: Int = 0
        private var imageProduct: String = ""
        private var total: Int = 0

        private var orderPlaced: Boolean = false
        private var dateOrderPlaced: String = ""

        private var orderConfirmed: Boolean = false
        private var dateOrderConfirmed: String = ""

        private var orderShipped: Boolean = false
        private var dateOrderShipped: String = ""

        private var outForDelivery: Boolean = false
        private var dateOutForDelivery: String = ""

        private var orderDelivered: Boolean = false
        private var dateOrderDelivered: String = ""

        private var reviewed: Boolean = false

        fun id(id: String) = apply { this.id = id }
        fun idUser(idUser: String) = apply { this.idUser = idUser }
        fun nameProduct(nameProduct: String) = apply { this.nameProduct = nameProduct }
        fun colorProduct(colorProduct: String) = apply { this.colorProduct = colorProduct }
        fun sizeProduct(sizeProduct: String) = apply { this.sizeProduct = sizeProduct }
        fun quantity(quantity: Int) = apply { this.quantity = quantity }
        fun imageProduct(imageProduct: String) = apply { this.imageProduct = imageProduct }
        fun total(total: Int) = apply { this.total = total }

        fun orderPlaced(orderPlaced: Boolean) = apply { this.orderPlaced = orderPlaced }
        fun dateOrderPlaced(dateOrderPlaced: String) =
            apply { this.dateOrderPlaced = dateOrderPlaced }

        fun orderConfirmed(orderConfirmed: Boolean) = apply { this.orderConfirmed = orderConfirmed }
        fun dateOrderConfirmed(dateOrderConfirmed: String) =
            apply { this.dateOrderConfirmed = dateOrderConfirmed }

        fun orderShipped(orderShipped: Boolean) = apply { this.orderShipped = orderShipped }
        fun dateOrderShipped(dateOrderShipped: String) =
            apply { this.dateOrderShipped = dateOrderShipped }

        fun outForDelivery(outForDelivery: Boolean) = apply { this.outForDelivery = outForDelivery }
        fun dateOutForDelivery(dateOutForDelivery: String) =
            apply { this.dateOutForDelivery = dateOutForDelivery }

        fun orderDelivered(orderDelivered: Boolean) = apply { this.orderDelivered = orderDelivered }
        fun dateOrderDelivered(dateOrderDelivered: String) =
            apply { this.dateOrderDelivered = dateOrderDelivered }

        fun reviewed(reviewed: Boolean) = apply { this.reviewed = reviewed }

        fun build() = OrderModel(
            id,
            idUser,
            nameProduct,
            colorProduct,
            sizeProduct,
            quantity,
            imageProduct,
            total,
            orderPlaced,
            dateOrderPlaced,
            orderConfirmed,
            dateOrderConfirmed,
            orderShipped,
            dateOrderShipped,
            outForDelivery,
            dateOutForDelivery,
            orderDelivered,
            dateOrderDelivered,
            reviewed
        )
    }

}