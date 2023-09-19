package com.example.shoptest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.ProduktItemBinding

class ProduktListAdapter(
    var dataset: List<Clothes>,
    var viewModel: MainViewModel
): RecyclerView.Adapter<ProduktListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ProduktItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ProduktItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var item = dataset[position]

        with(holder){
            binding.imageButton.setImageResource(R.drawable.baseline_favorite_border_24)
            binding.description2TV.text = item.description
            binding.imageView.load(item.image)
            binding.preis2TV.text= "${item.price} € "
        }
    }
}