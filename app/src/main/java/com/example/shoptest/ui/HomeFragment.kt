package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.ProduktAdapter
import com.example.shoptest.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel.loadData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getAllHerren().observe(viewLifecycleOwner){
            binding.herrenRV.adapter = ProduktAdapter(it,viewModel)
        }

        viewModel.getAllDamen().observe(viewLifecycleOwner){
            binding.damenRV.adapter = ProduktAdapter(it,viewModel)
        }

        binding.werbungIV1.setImageResource(R.drawable.summer)
        binding.werbung2IV.setImageResource(R.drawable.nike1)

    }
}