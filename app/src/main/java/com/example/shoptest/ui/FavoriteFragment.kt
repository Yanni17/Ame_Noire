package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.FavoriteAdapter
import com.example.shoptest.databinding.FragmentFavoriteBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentFavoriteBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Um die Sichtbarkeit anszuschalten:
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE


        var adapter = FavoriteAdapter(emptyList(), viewModel, requireContext())
        binding.favoriteRV.adapter = adapter

        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)

        // Ändere den Text des TextViews
        titleTextView.text = "${getString(R.string.favoriten3)}"

        val spinner = binding.spinner

        val spinnerItems = arrayOf(
            "${getString(R.string.zuletzt_hinzugefuegt)}",
            "${getString(R.string.preis_absteigend)}",
            "${getString(R.string.preis_aufsteigend)}"
        )

        val adapter2 =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        spinner.adapter = adapter2


        if (viewModel.firebaseAuth.currentUser != null) {
            binding.favoriteRV.visibility = View.VISIBLE
            viewModel.getAllLiked().observe(viewLifecycleOwner) {
                adapter.update(it)

                if (adapter.itemCount == 0) {
                    binding.emptyTextView.visibility = View.VISIBLE
                    binding.Int2TV.visibility = View.GONE
                    binding.aktuelleFavTV.visibility = View.GONE
                    binding.spinner.visibility = View.GONE
                } else {
                    binding.emptyTextView.visibility = View.GONE
                    binding.Int2TV.visibility = View.VISIBLE
                    binding.aktuelleFavTV.visibility = View.VISIBLE
                    binding.Int2TV.text = adapter.itemCount.toString()
                    binding.aktuelleFavTV.text = "${getString(R.string.artikel)}"
                    binding.spinner.visibility = View.VISIBLE
                }
            }
        } else {

            binding.favoriteCV.visibility = View.VISIBLE

            binding.anmeldeBTN.setOnClickListener {
                it.findNavController().navigate(R.id.loginFragment)
            }
            binding.registrierenBTN.setOnClickListener {
                it.findNavController().navigate(R.id.registerFragment2)
            }
        }

    }

}