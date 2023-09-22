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
import com.example.shoptest.adapter.KategorieAdapter
import com.example.shoptest.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentSearchBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.setToolbarTitle("Suche")

        viewModel.datasource.observe(viewLifecycleOwner) {
            binding.kategorieRV.adapter = KategorieAdapter(it, viewModel)
        }

    }
}