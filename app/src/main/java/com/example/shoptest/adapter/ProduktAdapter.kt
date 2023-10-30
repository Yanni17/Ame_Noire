package com.example.shoptest.adapter

import android.content.Context
import android.graphics.Color
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.ListItemBinding
import com.example.shoptest.ui.HomeFragmentDirections

class ProduktAdapter(
    private var dataset: List<Clothes>,
    private var viewModel: MainViewModel,
    private var context: Context
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
            binding.title2TV.text = item.title
            binding.preisTV.text = String.format("%.2f", item.price) + " €"
        }

        if (item.isLiked) {
            holder.binding.likeBTN.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            holder.binding.likeBTN.setImageResource(R.drawable.baseline_favorite_border_24)
        }

        holder.binding.likeBTN.setOnClickListener {

            if (viewModel.firebaseAuth.currentUser != null) {

                if (!item.isLiked) {

                    viewModel.addLikedItem(item.id)

                } else viewModel.removeLikedItem(item.id)

                viewModel.updateLike(!item.isLiked, item.id)

            } else {
                val toast = Toast.makeText(
                    context,
                    context.getString(R.string.notLoggedIn),
                    Toast.LENGTH_LONG
                )
                toast.show()
            }
        }

        holder.binding.cardView1.setOnClickListener { view ->

            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right) // Hier kannst du deine eigene Animation festlegen
                .setExitAnim(R.anim.slide_out_left) // Hier kannst du deine eigene Animation festlegen
                .build()

            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(item.id)
            view.findNavController().navigate(action, navOptions)
        }

    }

    fun update(list: List<Clothes>) {
        dataset = list
        notifyDataSetChanged()
    }
}