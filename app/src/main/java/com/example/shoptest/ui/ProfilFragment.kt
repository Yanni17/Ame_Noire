package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.databinding.FragmentProfilBinding
import com.google.android.material.appbar.MaterialToolbar

class ProfilFragment : Fragment() {

    val viewmodel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentProfilBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE

        val mainActivity = activity as MainActivity
        mainActivity.setToolbarTitle("Profil")

        if (viewmodel.firebaseAuth.currentUser != null) {
            binding.loggoutCV.visibility = View.VISIBLE
            binding.aktuellerNameTV.visibility = View.VISIBLE
            binding.angabenCV.visibility = View.VISIBLE
            binding.bestellungenCV.visibility = View.VISIBLE
            binding.begrueUngTV.visibility = View.VISIBLE
            binding.hilfeCV.visibility = View.VISIBLE
            binding.zahlungsmethodenCV.visibility = View.VISIBLE
            binding.werbung4IV.visibility = View.VISIBLE


            binding.loggoutCV.setOnClickListener {
                viewmodel.signOut()
                it.findNavController().navigate(R.id.loginFragment)
            }
        } else {
            binding.noaccountCV.visibility = View.VISIBLE

            binding.anmelde2BTN.setOnClickListener {
                it.findNavController().navigate(R.id.loginFragment)
            }
            binding.registrieren2BTN.setOnClickListener {
                it.findNavController().navigate(R.id.registerFragment2)
            }
        }

    }

}