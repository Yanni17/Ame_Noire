package com.example.shoptest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.ListItemBinding

class ProduktAdapter(
    private var dataset: List<Clothes>,
    private var viewModel: MainViewModel
) : RecyclerView.Adapter<ProduktAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = dataset[position]

        with(holder) {
            binding.produktIV.load(item.image)
            binding.produktDescriptionTV.text = item.description
            binding.likeBTN.setImageResource(R.drawable.baseline_favorite_border_24)
            binding.preisTV.text = "${item.price} €"
        }
    }
}