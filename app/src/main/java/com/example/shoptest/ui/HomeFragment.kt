package com.example.shoptest.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.ProduktAdapter
import com.example.shoptest.databinding.FragmentHomeBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding

    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
        viewModel.updateBadge(bottomNavigationView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.GONE

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

        val videoPath = "android.resource://${requireContext().packageName}/${R.raw.video3}"
        binding.werbungIV1.setVideoURI(Uri.parse(videoPath))


        binding.werbungIV1.setOnPreparedListener { mp ->
            mp.setVolume(0F, 0F)
            mp.start()

            // Animation
            val fadeOut = AlphaAnimation(1.8f, 0.0f)
            fadeOut.duration = 3000

            // Füge eine Listener hinzu, um die View nach Abschluss der Animation unsichtbar zu machen
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }
                override fun onAnimationEnd(animation: Animation?) {
                    binding.imageView.visibility = View.GONE
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })

            // Starte die Animation
            binding.imageView.startAnimation(fadeOut)
        }

        // wiederholung des Videos
        binding.werbungIV1.setOnCompletionListener { mp ->
            mp.setVolume(0F, 0F)
            mp.start()
        }

    }



}