package com.example.shoptest.data.datamodels

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothes_table")
data class Clothes(
    @PrimaryKey
    var id: Int,
    var title: String,
    var price: Double,
    var description: String,
    var category: String,
    var image: String,
    @Embedded
    var rating: Rating

    ) {
}