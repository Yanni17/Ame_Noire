package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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

        var navController = findNavController()

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

                    if (lastName.isNotBlank() && adress.isNotBlank() && zipCode.isNotBlank() && city.isNotBlank()) {
                        binding.lastNameET.setText(lastName)
                        binding.adressET.setText(adress)
                        binding.zipCodeET.setText(zipCode)
                        binding.cityET.setText(city)
                    }

                    binding.saveBTN.setOnClickListener {

                        val firstName1 = binding.firstNameET.text.toString().trim()
                        val lastName1 = binding.lastNameET.text.toString().trim()
                        val adress1 = binding.adressET.text.toString().trim()
                        val zipCode1 = binding.zipCodeET.text.toString().trim()
                        val city1 = binding.cityET.text.toString().trim()

                        if (lastName1.isNotBlank() && adress1.isNotBlank() && zipCode1.isNotBlank() && city1.isNotBlank()) {

                            viewModel.updateProfile(firstName1, lastName1, adress1, zipCode1, city1)

                            it.findNavController().navigateUp()

                        } else {
                            viewModel.showToast(requireContext().getString(R.string.alle_felder))
                        }
                    }
                }
                var cancelBtn = requireActivity().findViewById<ImageView>(R.id.cancel2BTN)

                cancelBtn.setOnClickListener {
                    navController.navigateUp()
                }

            }

        }

    }

}