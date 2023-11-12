package com.example.shoptest.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.payAdapter
import com.example.shoptest.data.datamodels.models.Profile
import com.example.shoptest.databinding.FragmentPayBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.ktx.toObject

class PayFragment : Fragment() {

    private lateinit var binding: FragmentPayBinding
    val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPayBinding.inflate(inflater, container, false)
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
        titleTextView.text = "${getString(R.string.checkout)}"

        var payAdapter = payAdapter(emptyList(), viewModel)
        binding.kasseRV.adapter = payAdapter

        viewModel.liveListOfCartItems.observe(viewLifecycleOwner) {
            payAdapter.update(it)
            binding.shippingTV.text = viewModel.berechneLieferzeitraum()

            var total = 0.0
            for (produkt in it) {
                val quantity = it.find { it.productId == produkt.productId }?.quantity ?: 0
                total += produkt.price * quantity
            }
            binding.sum3TV.text = String.format("%.2f €", total)
            binding.sumTV.text = String.format("%.2f €", (total + 7.99))
        }

        binding.arrowIB.setOnClickListener {
            it.findNavController().navigate(R.id.adressFragment)
        }

        viewModel.profileRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val profile = documentSnapshot.toObject<Profile>()
                if (profile != null) {

                    if(profile.nachName.isEmpty()){
                        binding.addAdressLL.visibility = View.VISIBLE
                    }else {
                        binding.adresseVollLL.visibility = View.VISIBLE
                        binding.vorNameTv.text = profile.vorName
                        binding.nachNameTv.text = profile.nachName
                        binding.adresseTv.text = profile.adresse
                        binding.stadtTv.text = profile.stadt
                        binding.plzTv.text = profile.plz

                        var view = binding.versandOptionenCV
                        var layoutParms = view.layoutParams as ConstraintLayout.LayoutParams
                        layoutParms.topMargin = 24
                        view.layoutParams = layoutParms

                        binding.edit3TV.setOnClickListener {
                            it.findNavController().navigate(R.id.adressFragment)
                        }

                    }

                }
            }

        }

        val navController = findNavController()

        binding.placeOrderBTN.setOnClickListener {

            //Alert
            val inflater = LayoutInflater.from(context)
            val customView = inflater.inflate(R.layout.custom_successfull_orderd, null)

            val container =
                (context as MainActivity).findViewById<FrameLayout>(R.id.framelayout)
            container.visibility = View.VISIBLE

            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(customView)
                .create()

            alertDialog.setCancelable(false)

            alertDialog.window?.setGravity(Gravity.CENTER)

            customView.alpha = 0.7f
            alertDialog.show()

            customView.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(null)

            var ok = customView.findViewById<MaterialButton>(R.id.button)

            ok.setOnClickListener {
                viewModel.clearList()
                alertDialog.dismiss()
                container.visibility = View.GONE

                navController.navigate(
                    R.id.homeFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build()
                )

            }
        }

    }

}