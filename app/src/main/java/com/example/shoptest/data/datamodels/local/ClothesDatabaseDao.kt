package com.example.shoptest.data.datamodels.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shoptest.data.datamodels.models.Clothes

@Dao
interface ClothesDatabaseDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Clothes>)

    @Query("SELECT * FROM clothes_table")
    fun getAll(): LiveData<List<Clothes>>

    @Query("SELECT * FROM clothes_table WHERE category = 'men''s clothing'")
    fun getMenClothingItems(): LiveData<List<Clothes>>

    @Query("SELECT * FROM clothes_table WHERE category = 'women''s clothing'")
    fun getWomenClothingItems(): LiveData<List<Clothes>>

    @Query("SELECT * FROM clothes_table WHERE category = 'electronics'")
    fun getElectronicsItems(): LiveData<List<Clothes>>

    @Query("SELECT * FROM clothes_table WHERE category = 'jewelery'")
    fun getJewelryItems(): LiveData<List<Clothes>>








}