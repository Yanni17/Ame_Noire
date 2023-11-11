package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Profile
import com.example.shoptest.databinding.FragmentProfilBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.ktx.toObject

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

        //View(Divider)
        val view = requireActivity().findViewById<View>(R.id.view)
        view.visibility = View.VISIBLE

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE
        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)
        titleTextView.text = "ACCOUNT"

        if (viewmodel.firebaseAuth.currentUser != null) {
            binding.loggoutCV.visibility = View.VISIBLE
            binding.aktuellerNameTV.visibility = View.VISIBLE
            binding.angabenCV.visibility = View.VISIBLE
            binding.bestellungenCV.visibility = View.VISIBLE
            binding.begrueUngTV.visibility = View.VISIBLE
            binding.hilfeCV.visibility = View.VISIBLE
            binding.zahlungsmethodenCV.visibility = View.VISIBLE
            binding.werbung4IV.visibility = View.VISIBLE
            binding.retourenCV.visibility = View.VISIBLE

            viewmodel.profileRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val profile = documentSnapshot.toObject<Profile>()
                    if (profile != null) {
                        val userName = profile.vorName.uppercase()
                        binding.aktuellerNameTV.text = userName
                    }
                }
            }

            binding.loggoutCV.setOnClickListener {
                viewmodel.signOut()
                it.findNavController().popBackStack(R.id.profilFragment,false)
                it.findNavController().navigate(R.id.profilFragment)

            }
        } else {

            binding.noaccountCV.visibility = View.VISIBLE

            binding.anmelde2BTN.setOnClickListener {
                it.findNavController().popBackStack()
                it.findNavController().navigate(R.id.loginFragment)
            }
            binding.registrieren2BTN.setOnClickListener {
                it.findNavController().popBackStack()
                it.findNavController().navigate(R.id.registerFragment2)
            }
        }

    }
}