package com.example.shoptest.data.datamodels.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shoptest.data.datamodels.Clothes

@Dao
interface ClothesDatabaseDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Clothes>)

    @Query("SELECT * FROM clothes_table")
    fun getAll(): LiveData<List<Clothes>>



}