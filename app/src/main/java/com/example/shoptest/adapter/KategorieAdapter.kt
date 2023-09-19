package com.example.shoptest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoptest.MainViewModel
import com.example.shoptest.data.datamodels.models.Kategorie
import com.example.shoptest.databinding.KategorieItemBinding


class KategorieAdapter(
    var dataset: List<Kategorie>,
    var viewModel: MainViewModel
): RecyclerView.Adapter<KategorieAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(val binding: KategorieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = KategorieItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = dataset[position]

        holder.binding.textView2.text = item.name

    }

}