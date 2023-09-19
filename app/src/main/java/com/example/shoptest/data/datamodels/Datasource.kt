package com.example.shoptest.data.datamodels


import com.example.shoptest.data.datamodels.models.Kategorie

class Datasource {

fun loadCategories(): List<Kategorie>{

    val kategorien = mutableListOf(
        Kategorie("Herren"),
        Kategorie("Damen"),
        Kategorie("Elektronik"),
        Kategorie("Schmuck"),

        )
    return kategorien

}
}