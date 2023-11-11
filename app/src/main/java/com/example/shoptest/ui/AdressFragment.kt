package com.example.shoptest.ui

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Profile
import com.example.shoptest.databinding.FragmentAdressBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.ktx.toObject

class AdressFragment : Fragment() {
    private lateinit var binding: FragmentAdressBinding
    val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE
        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)
        titleTextView.text = getString(R.string.versandadresse)

        binding.germanyIV.load(R.drawable.germany) {
            transformations(RoundedCornersTransformation(16f))
        }

        viewModel.profileRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val profile = documentSnapshot.toObject<Profile>()
                if (profile != null) {

                    val firstName = profile.vorName
                    val lastName = profile.nachName
                    val adress = profile.adresse
                    val zipCode = profile.plz
                    val city = profile.stadt

                    if (firstName.isNotBlank()) {
                        binding.firstNameET.setText(firstName)
                    }

                    if(lastName.isNotEmpty() && adress.isNotEmpty() && zipCode.isNotEmpty() && city.isNotEmpty()){
                        binding.lastNameET.setText(lastName)
                        binding.adressET.setText(adress)
                        binding.zipCodeET.setText(zipCode)
                        binding.cityET.setText(city)
                    }

                    binding.saveBTN.setOnClickListener {

                        val lastName1 = binding.lastNameET.text.toString()
                        val adress1 = binding.adressET.text.toString()
                        val zipCode1 = binding.zipCodeET.text.toString()
                        val city1 = binding.cityET.text.toString()

                        viewModel.updateProfile(firstName,lastName1,adress1,zipCode1,city1)

                        it.findNavController().navigateUp()

                    }


                }

            }

        }

    }

}