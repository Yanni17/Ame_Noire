package com.example.shoptest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.ProduktItemBinding
import com.example.shoptest.ui.ProductsFragmentDirections
import com.squareup.moshi.internal.Util

class ProduktListAdapter(
    var viewModel: MainViewModel,
    var context: Context
) : ListAdapter<Clothes, ProduktListAdapter.ItemViewHolder>(UtilDiffClothes()) {

    inner class ItemViewHolder(val binding: ProduktItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Clothes) {

            with(binding) {
                titleTV.text = item.title
                imageView.load(item.image)
                preis2TV.text = String.format("%.2f", item.price) + " €"

                if (item.isLiked) {
                    imageButton.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    imageButton.setImageResource(R.drawable.baseline_favorite_border_24)
                }

                imageButton.setOnClickListener {

                    if (viewModel.firebaseAuth.currentUser != null){

                        if(!item.isLiked){

                            viewModel.addLikedItem(item.id)

                        }else viewModel.removeLikedItem(item.id)

                        viewModel.updateLike(!item.isLiked, item.id)

                        if (item.isLiked) {
                            imageButton.setImageResource(R.drawable.baseline_favorite_24)
                        } else {
                            imageButton.setImageResource(R.drawable.baseline_favorite_border_24)
                        }
                    }else {
                        val toast = Toast.makeText(
                            context,
                            "Sie müssen sich erst Anmelden!",
                            Toast.LENGTH_LONG)
                        toast.show()
                    }



                }

                binding.cardView.setOnClickListener {
                    it.findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToDetailFragment(item.id))
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ProduktItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.bind(getItem(position))

    }

    private class UtilDiffClothes : DiffUtil.ItemCallback<Clothes>() {
        override fun areItemsTheSame(oldItem: Clothes, newItem: Clothes): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Clothes, newItem: Clothes): Boolean {
            return (oldItem.id == newItem.id
                    && oldItem.isLiked == newItem.isLiked
                    && oldItem.price == newItem.price
                    && oldItem.description == newItem.description
                    && oldItem.category == newItem.category
                    && oldItem.image == newItem.image
                    && oldItem.rating == newItem.rating
                    && oldItem.title == newItem.title)
        }
    }

}