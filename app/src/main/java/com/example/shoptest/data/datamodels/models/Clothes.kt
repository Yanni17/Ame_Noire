package com.example.shoptest.data.datamodels.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothes_table")
data class Clothes(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "price") var price: Double = 0.0,
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "category") var category: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "isLiked") var isLiked: Boolean = false,
    @Embedded
    var rating: Rating = Rating()

) {
}