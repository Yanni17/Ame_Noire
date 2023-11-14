package com.example.shoptest.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageButton
import androidx.fragment.app.Fragment
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
        viewModel.getAllClothes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelBtn = requireActivity().findViewById<ImageButton>(R.id.cancel2BTN)
        cancelBtn.visibility = View.GONE

        //View(Divider)
        val view = requireActivity().findViewById<View>(R.id.view)
        view.visibility = View.INVISIBLE

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.GONE

        var herrenAdapter = ProduktAdapter(emptyList(), viewModel, requireContext())
        binding.herrenRV.adapter = herrenAdapter

        var damenAdapter = ProduktAdapter(emptyList(), viewModel, requireContext())
        binding.damenRV.adapter = damenAdapter

        viewModel.getAllHerren().observe(viewLifecycleOwner) {
            herrenAdapter.update(it)
        }

        viewModel.getAllDamen().observe(viewLifecycleOwner) {
            damenAdapter.update(it)
        }

        // WERBUNG
        binding.werbung2IV.setImageResource(R.drawable.lfdy3)
        binding.werbung3IV.setImageResource(R.drawable.pegador)
        binding.werbung4IV.setImageResource(R.drawable.peso1)


        //Video
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