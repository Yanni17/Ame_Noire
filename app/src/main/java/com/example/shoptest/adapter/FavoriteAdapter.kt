package com.example.shoptest.adapter

import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.FavoriteItemBinding
import com.example.shoptest.ui.FavoriteFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteAdapter(
    private var dataset: List<Clothes>,
    private var viewModel: MainViewModel,
    private var context: Context,
    private var bottomNavigationView: BottomNavigationView
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
                binding.likeBtn.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding.likeBtn.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }
        holder.binding.likeBtn.setOnClickListener {

            if (!item.isLiked) {
                viewModel.addLikedItem(item.id)
            } else viewModel.removeLikedItem(item.id)
            viewModel.updateLike(!item.isLiked, item.id)
        }

        holder.binding.favortieCV.setOnClickListener {
            it.findNavController()
                .navigate(FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(item.id))
        }

        holder.binding.hinzufuegenBTN.setOnClickListener {
            viewModel.addToCart(item.id)
            viewModel.updateBadge(bottomNavigationView)

            // Aufblasen des Layouts
            val inflater = LayoutInflater.from(context)
            val customView = inflater.inflate(R.layout.custom_alert, null)

            // Erstellen und Anzeigen des AlertDialog
            val alertDialog = AlertDialog.Builder(context)
                .setView(customView)
                .create()

            // Setze die Position des Dialogs oben auf dem Bildschirm
            alertDialog.window?.setGravity(Gravity.TOP)

            // Setze die Anfangsalpha auf 0 (Fenster ist unsichtbar)
            customView.alpha = 0.3f
            alertDialog.show()

            // Erzeuge eine Einblendungsanimation
            customView.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(null)

            // 2 Sekunden warten und das Fenster verschwinden lassen
            val handler = Handler()
            handler.postDelayed({
                alertDialog.dismiss()
            }, 3000)

        }

    }

    fun update(list: List<Clothes>) {
        dataset = list
        notifyDataSetChanged()
    }
}