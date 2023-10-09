package com.example.shoptest.data.datamodels.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothes_table")
data class Clothes(
    @PrimaryKey
    var id: Int = 0,
    var title: String = "",
    var price: Double = 0.0,
    var description: String = "",
    var category: String = "",
    var image: String = "",
    var isLiked: Boolean = false,
    //var inCart: Boolean = false,
    @Embedded
    var rating: Rating = Rating()

    ) {
}