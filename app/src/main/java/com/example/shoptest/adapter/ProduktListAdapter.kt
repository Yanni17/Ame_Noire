package com.example.shoptest.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.ProduktItemBinding
import com.example.shoptest.ui.ProductsFragmentDirections
import com.google.android.material.button.MaterialButton

class ProduktListAdapter(
    var viewModel: MainViewModel,
    var context: Context
) : ListAdapter<Clothes, ProduktListAdapter.ItemViewHolder>(UtilDiffClothes()) {

    inner class ItemViewHolder(val binding: ProduktItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Clothes) {

            with(binding) {
                titleTV.text = item.title
                ivImage.load(item.image)
                preis2TV.text = String.format("%.2f", item.price) + " €"

                if (item.isLiked) {
                    imageButton.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    imageButton.setImageResource(R.drawable.baseline_favorite_border_24)
                }

                imageButton.setOnClickListener {

                    if (viewModel.firebaseAuth.currentUser != null) {

                        if (!item.isLiked) viewModel.addLikedItem(item.id) else viewModel.removeLikedItem(item.id)

                        viewModel.updateLike(!item.isLiked, item.id)

                        if (item.isLiked) {
                            imageButton.setImageResource(R.drawable.baseline_favorite_24)
                        } else {
                            imageButton.setImageResource(R.drawable.baseline_favorite_border_24)
                        }
                    } else {
//
                        // Alert
                        val inflater = LayoutInflater.from(context)
                        val customView = inflater.inflate(R.layout.custom_layout, null)

                        val container = (context as MainActivity).findViewById<FrameLayout>(R.id.framelayout)
                        container.visibility = View.VISIBLE

                        val alertDialog = AlertDialog.Builder(context)
                            .setView(customView)
                            .create()

                        alertDialog.setCancelable(false)
                        alertDialog.window?.setGravity(Gravity.CENTER)
                        customView.alpha = 0.7f
                        alertDialog.show()

                        customView.animate()
                            .alpha(1f)
                            .setDuration(1000)
                            .setListener(null)

                        var loginBtn = customView.findViewById<MaterialButton>(R.id.loginBTN1)
                        var cancelBtn = customView.findViewById<MaterialButton>(R.id.cancelBTN)

                        loginBtn.setOnClickListener {
                            binding.root.findNavController().navigate(R.id.loginFragment)
                            alertDialog.dismiss()
                            container.visibility = View.INVISIBLE

                        }

                        cancelBtn.setOnClickListener {
                            alertDialog.dismiss()
                            container.visibility = View.INVISIBLE

                        }
                    }
                }

                binding.ivImage.setOnClickListener { view ->
                    val action =
                        ProductsFragmentDirections.actionProductsFragmentToDetailFragment(item.id)
                    view.findNavController().navigate(action)

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