package com.example.shoptest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainViewModel
import com.example.shoptest.data.datamodels.models.CartItem
import com.example.shoptest.databinding.CartItemBinding


class CartAdapter(
    private var dataset: List<CartItem>,
    private var viewModel: MainViewModel,
) : RecyclerView.Adapter<CartAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.binding.preis5TV.text = String.format("%.2f", item.price) + " €"
        holder.binding.title5TV.text = item.title
        holder.binding.imageView5.load(item.image)

        holder.binding.IntTV.text = item.quantity.toString()

        holder.binding.deleteBTN.setOnClickListener {
            viewModel.removeCartLive(item.productId)
            notifyDataSetChanged()
        }


    }

    fun update(list: List<CartItem>) {
        dataset = list
        notifyDataSetChanged()
    }

}
