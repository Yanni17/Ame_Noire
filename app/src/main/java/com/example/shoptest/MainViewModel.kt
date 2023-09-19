package com.example.shoptest

import ClothesApi
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoptest.data.datamodels.AppRepository
import com.example.shoptest.data.datamodels.Datasource
import com.example.shoptest.data.datamodels.local.getDatabase
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.data.datamodels.models.Kategorie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)

    private val repository = AppRepository(ClothesApi,database)

    private val _datasource = MutableLiveData<List<Kategorie>>()
    val datasource : LiveData<List<Kategorie>>
        get() = _datasource

    init {
        _datasource.postValue(Datasource().loadCategories())
        loadData()
    }

    fun loadData(){

        viewModelScope.launch(Dispatchers.IO) {
            repository.getClothes()
        }

    }

    fun getAllHerren(): LiveData<List<Clothes>>{
            return repository.getAllHerren()
    }

    fun getAllDamen(): LiveData<List<Clothes>>{
        return repository.getAllDamen()
    }

    fun getAllElectrics(): LiveData<List<Clothes>>{
        return repository.getAllElectrics()
    }

    fun getAllJewelry(): LiveData<List<Clothes>>{
        return repository.getAllJewelry()
    }
}