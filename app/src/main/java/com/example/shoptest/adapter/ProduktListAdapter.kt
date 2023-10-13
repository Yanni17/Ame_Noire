package com.example.shoptest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
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

                binding.cardView.setOnClickListener { view ->
                    // Hier kannst du die gewünschte Animation erstellen und auf das ImageView anwenden
                    val scaleUpAnimation = ScaleAnimation(
                        1f, 2f, // Start- und Endskalierungsfaktor in X-Richtung
                        1f, 2f, // Start- und Endskalierungsfaktor in Y-Richtung
                        Animation.RELATIVE_TO_SELF, 0.5f, // Punkt, um den skaliert wird (hier: Mitte X)
                        Animation.RELATIVE_TO_SELF, 0.5f  // Punkt, um den skaliert wird (hier: Mitte Y)
                    )
                    scaleUpAnimation.duration = 300 // Dauer der Animation in Millisekunden

                    // Setze einen Listener für die Animation
                    scaleUpAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            // Die Animation startet
                            binding.preis2TV.visibility = View.GONE
                            binding.imageButton.visibility = View.GONE
                            binding.titleTV.visibility = View.GONE
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            // Hier kannst du die Navigation zum Detailfragment starten
                            val imageTransitionName = view.context.getString(R.string.animation)
                            val extras = FragmentNavigatorExtras(
                                binding.imageView to imageTransitionName
                            )

                            val action = ProductsFragmentDirections.actionProductsFragmentToDetailFragment(item.id)
                            view.findNavController().navigate(action, extras)
                        }

                        override fun onAnimationRepeat(animation: Animation?) {
                            // Die Animation wiederholt sich (normalerweise nicht relevant)
                        }
                    })

                    // Füge die Animation zum ImageView hinzu
                    binding.imageView.startAnimation(scaleUpAnimation)
                }


//                binding.cardView.setOnClickListener {
//
//                    val navOptions = NavOptions.Builder()
//                        .setEnterAnim(R.anim.slide_in_right) // Hier kannst du deine eigene Animation festlegen
//                        .setExitAnim(R.anim.slide_out_left) // Hier kannst du deine eigene Animation festlegen
//                        .build()
//
//                    it.findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToDetailFragment(item.id))
//                }

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