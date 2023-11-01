package com.example.shoptest.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.CartItemBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class CartAdapter(
    private var dataset: List<Clothes>,
    private var viewModel: MainViewModel,
    private var bottomNavigationView: BottomNavigationView
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

        var item = dataset[position]

        holder.binding.preis5TV.text = String.format("%.2f", item.price) + " €"
        holder.binding.title5TV.text = item.title
        holder.binding.imageView5.load(item.image)

        var cartList = viewModel.listOfCartItems
        var produkt = cartList.find { it.productId == item.id }

        Log.e("Cartlist", "$cartList")
        holder.binding.IntTV.text = produkt!!.quantity.toString()

        holder.binding.imageButton3.setOnClickListener {
            viewModel.removeFromCart(item.id)

            it.findNavController().navigate(R.id.cashoutFragment)
        }

    }

    fun updateData(list: List<Clothes>) {
        dataset = list
        notifyDataSetChanged()
    }

}
