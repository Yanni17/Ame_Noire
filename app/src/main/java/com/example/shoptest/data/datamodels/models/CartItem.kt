package com.example.shoptest.data.datamodels.models

data class CartItem(
    val productId: Int = 0,
    var quantity: Int = 0,
    var title: String = "",
    var price: Double = 0.0,
    var image: String = ""
)
