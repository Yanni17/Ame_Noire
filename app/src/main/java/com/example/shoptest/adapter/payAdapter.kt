package com.example.shoptest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainViewModel
import com.example.shoptest.data.datamodels.models.CartItem
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.PayItemBinding

class payAdapter(
    private var dataset: List<CartItem>,
    private var viewModel: MainViewModel
): RecyclerView.Adapter<payAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: PayItemBinding ) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = PayItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var item = dataset[position]
        holder.binding.imageView6.load(item.image)

    }

    fun update(list: List<CartItem>) {
        dataset = list
        notifyDataSetChanged()
    }

}