package com.example.shoptest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Kategorie
import com.example.shoptest.databinding.KategorieItemBinding
import com.example.shoptest.ui.SearchFragmentDirections


class KategorieAdapter(
    var dataset: List<Kategorie>,
    var viewModel: MainViewModel
) : RecyclerView.Adapter<KategorieAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: KategorieItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            KategorieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = dataset[position]

        holder.binding.textView2.text = item.name

        holder.binding.kategorieCV.setOnClickListener {

            val action = SearchFragmentDirections.actionSearchFragmentToProductsFragment(item.name)

            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .build()

            it.findNavController().navigate(action, navOptions)
        }

    }

}