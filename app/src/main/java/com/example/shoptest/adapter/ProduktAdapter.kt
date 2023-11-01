package com.example.shoptest.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.ListItemBinding
import com.example.shoptest.ui.HomeFragmentDirections
import com.google.android.material.button.MaterialButton


class ProduktAdapter(
    private var dataset: List<Clothes>,
    private var viewModel: MainViewModel,
    private var context: Context,
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

//        var dialog = Dialog(context)
//        dialog.setContentView (R.layout.custom_layout)
//        dialog.window?.setBackgroundDrawable(AppCompatResources.getDrawable(context,R.drawable.dialog_background))
//        dialog.window?.setLayout (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        dialog.setCancelable(true)
//
//        var cancelBTN = dialog.findViewById<MaterialButton>(R.id.cancelBTN)
//        var loginBTN = dialog.findViewById<MaterialButton>(R.id.loginBTN1)
//
//        cancelBTN.setOnClickListener {
//            holder.binding.root.findNavController().navigate(R.id.homeFragment)
//            dialog.dismiss()
//        }


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
                //dialog.show()
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
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
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