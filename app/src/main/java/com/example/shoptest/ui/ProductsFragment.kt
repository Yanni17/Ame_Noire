package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.ProduktListAdapter
import com.example.shoptest.databinding.FragmentProductsBinding

class ProductsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentProductsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var kategorie = requireArguments().getString("name")

        when (kategorie){
            "Herren" -> viewModel.getAllHerren().observe(viewLifecycleOwner){
                binding.produktRV.adapter = ProduktListAdapter(it,viewModel)
            }
            "Damen" -> viewModel.getAllDamen().observe(viewLifecycleOwner){
                binding.produktRV.adapter = ProduktListAdapter(it,viewModel)
            }
            "Elektronik" -> viewModel.getAllElectrics().observe(viewLifecycleOwner){
                binding.produktRV.adapter = ProduktListAdapter(it,viewModel)
            }
            "Schmuck" -> viewModel.getAllJewelry().observe(viewLifecycleOwner){
                binding.produktRV.adapter = ProduktListAdapter(it,viewModel)
            }
        }

    }

}