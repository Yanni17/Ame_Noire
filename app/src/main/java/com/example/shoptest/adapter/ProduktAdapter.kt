package com.example.shoptest.adapter

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentContainer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoptest.MainActivity
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


        if (item.isLiked) {
            holder.binding.likeBTN.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            holder.binding.likeBTN.setImageResource(R.drawable.baseline_favorite_border_24)
        }

        holder.binding.likeBTN.setOnClickListener {

            if (viewModel.firebaseAuth.currentUser != null) {

                if (!item.isLiked) viewModel.addLikedItem(item.id) else viewModel.removeLikedItem(item.id)

                viewModel.updateLike(!item.isLiked, item.id)

            } else {

                //Alert
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
                    holder.binding.root.findNavController().navigate(R.id.loginFragment)
                    alertDialog.dismiss()
                    container.visibility = View.INVISIBLE
                }
                cancelBtn.setOnClickListener {
                    alertDialog.dismiss()
                    container.visibility = View.INVISIBLE

                }


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