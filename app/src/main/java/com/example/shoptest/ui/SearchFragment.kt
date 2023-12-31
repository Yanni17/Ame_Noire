package com.example.shoptest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.KategorieAdapter
import com.example.shoptest.databinding.FragmentSearchBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentSearchBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //View(Divider)
        val view = requireActivity().findViewById<View>(R.id.view)
        view.visibility = View.VISIBLE

        // Ändere den Text des TextViews
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE
        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)
        titleTextView.text = (requireActivity().getString(R.string.suche))

        // Um die Sichtbarkeit anszuschalten:
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE

        binding.imageView2.setImageResource(R.drawable.test31)

        viewModel.datasource.observe(viewLifecycleOwner) {
            binding.kategorieRV.adapter = KategorieAdapter(it, viewModel)
        }


    }
}