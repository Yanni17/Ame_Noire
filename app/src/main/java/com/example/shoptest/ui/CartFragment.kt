package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.CartAdapter
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.data.datamodels.models.Profile
import com.example.shoptest.databinding.FragmentCashoutBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCashoutBinding
    val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCashoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val cartAdapter = CartAdapter(emptyList(), viewModel,bottomNavigationView)
        binding.cartRV.adapter = cartAdapter

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE

        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)

        // Ändere den Text des TextViews
        titleTextView.text = "${getString(R.string.warenkorb1)}"

        viewModel.allClothes.observe(viewLifecycleOwner) { clothesList ->
            // Dieser Codeblock wird aufgerufen, wenn sich die Daten in allClothes ändern
            if (viewModel.firebaseAuth.currentUser != null) {

                val userId = viewModel.firebaseAuth.currentUser!!.uid
                val profileRef = viewModel.firestore.collection("Profile").document(userId)

                profileRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val profile = documentSnapshot.toObject(Profile::class.java)
                        val cartList = profile?.cartList // Warenkorbliste

                        val productsInCart: List<Clothes> = clothesList.filter { clothes ->

                            cartList!!.any { it.productId == clothes.id }
                        }
                        cartAdapter.updateData(productsInCart)

                        var total = 0.0

                        if (cartAdapter.itemCount == 0) {
                            binding.noBasketTV.visibility = View.VISIBLE
                        } else {
                            binding.noBasketTV.visibility = View.GONE
                            binding.cartRV.visibility = View.VISIBLE
                            binding.bezahlenBTN.visibility = View.VISIBLE
                            binding.priceTV.visibility = View.VISIBLE
                            binding.totalTV.visibility = View.VISIBLE
                            binding.lieferkostenTV.visibility = View.VISIBLE

                            for (produkt in productsInCart) {
                                val quantity = cartList?.find { it.productId == produkt.id }?.quantity ?: 0
                                total += produkt.price * quantity
                            }
                            binding.priceTV.text = String.format("%.2f €", total)
                        }

                    } else {
                        // Das Profil des Benutzers existiert nicht.
                    }
                }.addOnFailureListener { exception ->
                    // Fehler bei der Firestore-Abfrage.
                }
            } else {
                binding.cashoutCV.visibility = View.VISIBLE

                binding.anmeldeBTN.setOnClickListener {
                    it.findNavController().popBackStack(R.id.loginFragment,false)
                    it.findNavController().navigate(R.id.loginFragment)
                }
                binding.registrierenBTN.setOnClickListener {
                    it.findNavController().popBackStack(R.id.registerFragment2,false)
                    it.findNavController().navigate(R.id.registerFragment2)
                }
            }
        }


    }

}