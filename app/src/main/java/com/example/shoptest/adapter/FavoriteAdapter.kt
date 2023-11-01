package com.example.shoptest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.FavoriteItemBinding
import com.example.shoptest.ui.FavoriteFragmentDirections
import com.google.android.material.snackbar.Snackbar

class FavoriteAdapter(
    private var dataset: List<Clothes>,
    private var viewModel: MainViewModel,
    private var context: Context
) : RecyclerView.Adapter<FavoriteAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var item = dataset[position]

        with(holder) {
            binding.imageView3.load(item.image)
            binding.title3TV.text = item.title
            binding.preis3TV.text = String.format("%.2f", item.price) + " €"

            if (item.isLiked) {
                binding.imageButton2.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding.imageButton2.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }
        holder.binding.imageButton2.setOnClickListener {

            if (!item.isLiked) {
                viewModel.addLikedItem(item.id)
            } else viewModel.removeLikedItem(item.id)
            viewModel.updateLike(!item.isLiked, item.id)
        }

        holder.binding.favortieCV.setOnClickListener {
            it.findNavController()
                .navigate(FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(item.id))
        }

        holder.binding.button.setOnClickListener {
            viewModel.addToCart(item.id)
            val message = context.getString(R.string.erfolgreichWarenkorb)
            val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_SHORT)

            snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.grey))
            val textView =
                snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(context.getColor(R.color.text))

            snackbar.show()
        }


    }

    fun update(list: List<Clothes>) {
        dataset = list
        notifyDataSetChanged()
    }
}