package com.example.shoptest

import ClothesApi
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoptest.data.datamodels.AppRepository
import com.example.shoptest.data.datamodels.local.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)

    private val repository = AppRepository(ClothesApi,database)

    init {
        loadData()
    }

    fun loadData(){

        viewModelScope.launch(Dispatchers.IO) {
            repository.getClothes()
        }

    }
}