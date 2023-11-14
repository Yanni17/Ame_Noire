package com.example.shoptest.data.datamodels


import android.content.Context
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Kategorie

class Datasource(
    val context: Context
) {

    fun loadCategories(): List<Kategorie> {
        return mutableListOf(
            Kategorie(context.getString(R.string.herren)),
            Kategorie(context.getString(R.string.damen)),
            Kategorie(context.getString(R.string.schmuck)),

            )
    }


}