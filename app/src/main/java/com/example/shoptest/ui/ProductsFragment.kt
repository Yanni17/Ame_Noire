package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.ProduktListAdapter
import com.example.shoptest.databinding.FragmentProductsBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentProductsBinding

    private val produktListAdapter: ProduktListAdapter by lazy { ProduktListAdapter(viewModel,requireContext()) }

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

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE

        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)

        var kategorie = requireArguments().getString("name")

        binding.produktRV.adapter = produktListAdapter

        //Adapter anpassen
        when (kategorie) {

            "Herren","Men" -> viewModel.getAllHerren().observe(viewLifecycleOwner) {
                produktListAdapter.submitList(it)
                titleTextView.text = "${getString(R.string.herren1)}"
            }

            "Damen","Women" -> viewModel.getAllDamen().observe(viewLifecycleOwner) {
                produktListAdapter.submitList(it)
                titleTextView.text = "${getString(R.string.damen1)}"
            }

            "Elektronik","Electronics" -> viewModel.getAllElectrics().observe(viewLifecycleOwner) {
                produktListAdapter.submitList(it)
                titleTextView.text = "${getString(R.string.elektronik1)}"
            }

            "Schmuck","Jewelry" -> viewModel.getAllJewelry().observe(viewLifecycleOwner) {
                produktListAdapter.submitList(it)
                titleTextView.text = "${getString(R.string.schmuck1)}"
            }
        }
    }
}