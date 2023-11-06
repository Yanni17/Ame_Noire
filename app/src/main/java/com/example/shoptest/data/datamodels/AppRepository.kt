package com.example.shoptest.data.datamodels

import ClothesApi
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.shoptest.data.datamodels.local.ClothesDatabase
import com.example.shoptest.data.datamodels.models.Clothes
import java.lang.Exception

const val TAG = "AppRepositoryTAG"

class AppRepository(private val api: ClothesApi, private val database: ClothesDatabase) {

    val allClothes = database.clothesDatabaseDao.getAll()

    suspend fun getClothes() {

        try {
            if (database.clothesDatabaseDao.count() == 0){

                var clothes = api.retrofitService.getAllProducts()
                database.clothesDatabaseDao.insertAll(
                    clothes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Data from API: $e")
        }
    }

    fun getAllHerren(): LiveData<List<Clothes>> {
        return database.clothesDatabaseDao.getMenClothingItems()
    }

    fun getAllDamen(): LiveData<List<Clothes>> {
        return database.clothesDatabaseDao.getWomenClothingItems()
    }

    fun getAllElectrics(): LiveData<List<Clothes>> {
        return database.clothesDatabaseDao.getElectronicsItems()
    }

    fun getAllJewelry(): LiveData<List<Clothes>> {
        return database.clothesDatabaseDao.getJewelryItems()
    }

    fun getAllLiked(): LiveData<List<Clothes>> {
        return database.clothesDatabaseDao.getAllLiked()
    }

    fun updateLike(liked: Boolean, id: Int) {
        try {
            database.clothesDatabaseDao.updateLike(liked, id)
        } catch (e: Exception) {
            Log.e(TAG, "Error update Like: $e")
        }
    }

    fun getDetail(id: Int): LiveData<Clothes> {
        return database.clothesDatabaseDao.getDetails(id)
    }

}