package com.example.shoptest.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.ProduktAdapter
import com.example.shoptest.databinding.FragmentHomeBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Timer
import java.util.TimerTask

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

        // Um die Sichtbarkeit anszuschalten:
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.GONE

        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)

        // Text der TextViews
        titleTextView.text = "HOME"

        var adapter = ProduktAdapter(emptyList(), viewModel, requireContext())
        binding.herrenRV.adapter = adapter

        var adapter2 = ProduktAdapter(emptyList(), viewModel, requireContext())
        binding.damenRV.adapter = adapter2

        viewModel.getAllHerren().observe(viewLifecycleOwner) {
            adapter.update(it)
        }

        viewModel.getAllDamen().observe(viewLifecycleOwner) {
            adapter2.update(it)
        }

        // WERBUNG
        binding.werbung2IV.setImageResource(R.drawable.lfdy3)
        binding.werbung3IV.setImageResource(R.drawable.pegador)
        binding.werbung4IV.setImageResource(R.drawable.peso1)

        val videoPath = "android.resource://${requireContext().packageName}/${R.raw.video2}"
        binding.werbungIV1.setVideoURI(Uri.parse(videoPath))


        // Vorpuffern des Videos
        binding.werbungIV1.setOnPreparedListener { mp ->
            mp.setVolume(0F, 0F)
            mp.start()
        }

        // Optional: Überwache den Abschluss des Videos und wiederhole es bei Bedarf
        binding.werbungIV1.setOnCompletionListener { mp ->
            mp.setVolume(0F, 0F)
            mp.start()
        }

    }

}