package com.example.shoptest.ui

import android.os.Bundle
import android.util.Log
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
import com.example.shoptest.databinding.FragmentCashoutBinding
import com.google.android.material.appbar.MaterialToolbar


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

        //View(Divider)
        val view = requireActivity().findViewById<View>(R.id.view)
        view.visibility = View.VISIBLE

        // Ändere den Text des TextViews
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE
        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)
        titleTextView.text = "${getString(R.string.warenkorb1)}"

        var cartAdapter = CartAdapter(emptyList(), viewModel)
        binding.cartRV.adapter = cartAdapter

        viewModel.liveListOfCartItems.observe(viewLifecycleOwner) { cartItemsList ->

            Log.d("Delete", "bin da")
            if (viewModel.firebaseAuth.currentUser != null) {
                cartAdapter.update(cartItemsList)
                var total = 0.0

                if (cartItemsList.isEmpty()) {
                    binding.noBasketTV.visibility = View.VISIBLE
                    binding.cartRV.visibility = View.GONE
                    binding.bezahlenBTN.visibility = View.GONE
                    binding.priceTV.visibility = View.GONE
                    binding.totalTV.visibility = View.GONE
                    binding.lieferkostenTV.visibility = View.GONE
                } else {
                    binding.noBasketTV.visibility = View.GONE
                    binding.cartRV.visibility = View.VISIBLE
                    binding.bezahlenBTN.visibility = View.VISIBLE
                    binding.priceTV.visibility = View.VISIBLE
                    binding.totalTV.visibility = View.VISIBLE
                    binding.lieferkostenTV.visibility = View.VISIBLE

                    for (produkt in cartItemsList) {
                        val quantity =
                            cartItemsList.find { it.productId == produkt.productId }?.quantity ?: 0
                        total += produkt.price * quantity
                    }
                    binding.priceTV.text = String.format("%.2f €", total)
                }

            } else {
                binding.cashoutCV.visibility = View.VISIBLE

                binding.anmeldeBTN.setOnClickListener {
                    it.findNavController().navigate(R.id.loginFragment)
                }
                binding.registrierenBTN.setOnClickListener {
                    it.findNavController().navigate(R.id.registerFragment2)
                }
            }
        }

        binding.bezahlenBTN.setOnClickListener {

            it.findNavController().navigate(R.id.payFragment)

        }
    }


}
