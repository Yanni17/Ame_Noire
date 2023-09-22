package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.FavoriteAdapter
import com.example.shoptest.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentFavoriteBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.setToolbarTitle("Favoriten")


        var adapter = FavoriteAdapter(emptyList(),viewModel)
        binding.favoriteRV.adapter = adapter

        viewModel.getAllLiked().observe(viewLifecycleOwner){

            adapter.update(it)

            if(adapter.itemCount == 0){

                binding.emptyTextView.visibility = View.VISIBLE

            }else {

                binding.emptyTextView.visibility = View.GONE

            }
        }

    }

}