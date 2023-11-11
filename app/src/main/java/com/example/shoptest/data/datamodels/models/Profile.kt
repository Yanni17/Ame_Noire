package com.example.shoptest.data.datamodels.models

class Profile(
    var vorName: String = "",
    var telefonNummer: String = "",
    var likedList: List<Int> = emptyList(),
    val cartList: List<CartItem> = emptyList(),
    val nachName: String = "",
    val adresse: String = "",
    val stadt: String = "",
    val plz: String = ""


)
