package com.example.shoptest.data.datamodels.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoptest.data.datamodels.models.Clothes

@Database(entities = [Clothes::class], version = 1)
abstract class ClothesDatabase : RoomDatabase() {

    abstract val clothesDatabaseDao: ClothesDatabaseDao

}

private lateinit var INSTANCE: ClothesDatabase

fun getDatabase(context: Context): ClothesDatabase {
    synchronized(ClothesDatabase::class.java) {

        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                ClothesDatabase::class.java,
                "clothes_database"
            ).allowMainThreadQueries().build()

        }
    }
    return INSTANCE
}