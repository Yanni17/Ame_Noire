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
import com.example.shoptest.adapter.ProduktListAdapter
import com.example.shoptest.databinding.FragmentProductsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentProductsBinding

    private val produktListAdapter: ProduktListAdapter by lazy { ProduktListAdapter(viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Um die Sichtbarkeit anszuschalten:
        bottomNavigationView.visibility = View.VISIBLE

        val mainActivity = activity as MainActivity

        var kategorie = requireArguments().getString("name")

        binding.produktRV.adapter = produktListAdapter


        when (kategorie) {
            "Herren" -> viewModel.getAllHerren().observe(viewLifecycleOwner) {
                produktListAdapter.submitList(it)
                mainActivity.setToolbarTitle("Herren")
            }

            "Damen" -> viewModel.getAllDamen().observe(viewLifecycleOwner) {
                produktListAdapter.submitList(it)
                mainActivity.setToolbarTitle("Damen")
            }

            "Elektronik" -> viewModel.getAllElectrics().observe(viewLifecycleOwner) {
                produktListAdapter.submitList(it)
                mainActivity.setToolbarTitle("Elektronik")
            }

            "Schmuck" -> viewModel.getAllJewelry().observe(viewLifecycleOwner) {
                produktListAdapter.submitList(it)
                mainActivity.setToolbarTitle("Schmuck")
            }
        }

    }

}