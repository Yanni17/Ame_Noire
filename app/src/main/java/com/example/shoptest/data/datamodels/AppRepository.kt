package com.example.shoptest.data.datamodels

import ClothesApi
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.shoptest.data.datamodels.local.ClothesDatabase
import com.example.shoptest.data.datamodels.models.Clothes
import java.lang.Exception

const val TAG = "AppRepositoryTAG"

class AppRepository(private val api: ClothesApi,private val database: ClothesDatabase) {

    val allClothes = database.clothesDatabaseDao.getAll()
//    val allHerren = database.clothesDatabaseDao.getMenClothingItems()
//    val allDamen = database.clothesDatabaseDao.getWomenClothingItems()
//    val allElectrics = database.clothesDatabaseDao.getElectronicsItems()
//    val allJewelry = database.clothesDatabaseDao.getJewelryItems()

    suspend fun getClothes(){
        try {

            var clothes = api.retrofitService.getAllProducts()
            Log.e(TAG,clothes.toString())
            database.clothesDatabaseDao.insertAll(clothes)

        }catch (e: Exception){
            Log.e(TAG,"Error loading Data from API: $e")
        }
    }

    fun getAllHerren(): LiveData<List<Clothes>>{
        return database.clothesDatabaseDao.getMenClothingItems()
    }

    fun getAllDamen(): LiveData<List<Clothes>>{
        return database.clothesDatabaseDao.getWomenClothingItems()
    }

    fun getAllElectrics(): LiveData<List<Clothes>>{
        return database.clothesDatabaseDao.getElectronicsItems()
    }
    fun getAllJewelry(): LiveData<List<Clothes>>{
        return database.clothesDatabaseDao.getJewelryItems()
    }

}