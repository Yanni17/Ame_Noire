package com.example.shoptest.ui

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import coil.load
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.databinding.FragmentDetailBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Um die Sichtbarkeit anszuschalten:
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE

        // Ändere den Text des TextViews
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE
        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)
        titleTextView.text = "${getString(R.string.produkt_details)}"

        //Argument holen
        var id = requireArguments().getInt("id")

        viewModel.getDetail(id).observe(viewLifecycleOwner) { item ->

            with(binding) {
                ivImage.load(item.image)
                preis4TV.text = String.format("%.2f", item.price) + " €"
                descriptionTV2.text = item.description
                title4TV.text = item.title
                ratingBTN2.setImageResource(R.drawable.baseline_star_24)
                ratingTV2.text = item.rating.rate.toString()
                if (item.isLiked) {
                    likeBTN2.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    likeBTN2.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }

            binding.warenkorbBTN.setOnClickListener {

                if (viewModel.firebaseAuth.currentUser != null) {

                    viewModel.addToCartLive(item.id,item.title,item.price,item.image)

                    // Aufblasen des Layouts
                    val inflater = LayoutInflater.from(context)
                    val customView = inflater.inflate(R.layout.custom_alert, null)

                    // Erstellen und Anzeigen des AlertDialog
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setView(customView)
                        .create()

                    // Setze die Position des Dialogs oben auf dem Bildschirm
                    alertDialog.window?.setGravity(Gravity.TOP)

                    // Setze die Anfangsalpha auf 0 (Fenster ist unsichtbar)
                    customView.alpha = 0.3f
                    alertDialog.show()

                    // Erzeuge eine Einblendungsanimation
                    customView.animate()
                        .alpha(1f)
                        .setDuration(1000)
                        .setListener(null)

                    // 2 Sekunden warten und das Fenster verschwinden lassen
                    val handler = Handler()
                    handler.postDelayed({
                        alertDialog.dismiss()
                    }, 3000)

                } else {
                    //Alert
                    val inflater = LayoutInflater.from(context)
                    val customView = inflater.inflate(R.layout.custom_layout, null)

                    val container = (context as MainActivity).findViewById<FrameLayout>(R.id.framelayout)
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

                    var loginBtn = customView.findViewById<MaterialButton>(R.id.loginBTN2)
                    var cancelBtn = customView.findViewById<MaterialButton>(R.id.cancelBTN)

                    loginBtn.setOnClickListener {
                        binding.root.findNavController().navigate(R.id.loginFragment)
                        alertDialog.dismiss()
                        container.visibility = View.INVISIBLE

                    }

                    cancelBtn.setOnClickListener {
                        alertDialog.dismiss()
                        container.visibility = View.INVISIBLE

                    }
                }

            }

            binding.likeBTN2.setOnClickListener {
                if (viewModel.firebaseAuth.currentUser != null) {

                    if (!item.isLiked) {
                        viewModel.addLikedItem(item.id)
                    } else viewModel.removeLikedItem(item.id)
                    viewModel.updateLike(!item.isLiked, item.id)

                } else {

                    val inflater = LayoutInflater.from(context)
                    val customView = inflater.inflate(R.layout.custom_layout, null)

                    val container = (context as MainActivity).findViewById<FrameLayout>(R.id.framelayout)
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

                    var loginBtn = customView.findViewById<MaterialButton>(R.id.loginBTN2)
                    var cancelBtn = customView.findViewById<MaterialButton>(R.id.cancelBTN)

                    loginBtn.setOnClickListener {
                        binding.root.findNavController().navigate(R.id.loginFragment)
                        alertDialog.dismiss()
                        container.visibility = View.INVISIBLE

                    }

                    cancelBtn.setOnClickListener {
                        alertDialog.dismiss()
                        container.visibility = View.INVISIBLE

                    }
                }

            }

        }

    }

}