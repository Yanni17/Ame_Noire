package com.example.shoptest.data.datamodels.models

class Profile(
    var name: String = "",
    var telefonNummer: String = "",
    var likedList: List<Int> = emptyList(),
    var cartList: List<CartItem> = emptyList(),

)
