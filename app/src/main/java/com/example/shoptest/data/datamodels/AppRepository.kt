package com.example.shoptest.data.datamodels

import ClothesApi
import android.util.Log
import com.example.shoptest.data.datamodels.local.ClothesDatabase
import java.lang.Exception

const val TAG = "AppRepositoryTAG"

class AppRepository(private val api: ClothesApi,private val database: ClothesDatabase) {

    val clothes = database.clothesDatabaseDao.getAll()

    suspend fun getClothes(){
        try {

            var clothes = api.retrofitService.getAllProducts()
            Log.e(TAG,clothes.toString())
            database.clothesDatabaseDao.insertAll(clothes)

        }catch (e: Exception){
            Log.e(TAG,"Error loading Data from API: $e")
        }
    }

}