package com.example.shoptest.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionInflater
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.FragmentDetailBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

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

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE

        // Ändere den Text des TextViews
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

                    viewModel.addToCart(item.id)
                    viewModel.updateBadge(bottomNavigationView)

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
                    val toast = Toast.makeText(
                        requireContext(),
                        "${requireContext().getString(R.string.notLoggedIn)}",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }

            }

            binding.likeBTN2.setOnClickListener {

                if (viewModel.firebaseAuth.currentUser != null) {

                    if (!item.isLiked) {
                        viewModel.addLikedItem(item.id)
                    } else viewModel.removeLikedItem(item.id)
                    viewModel.updateLike(!item.isLiked, item.id)

                } else {
                    val toast = Toast.makeText(
                        requireContext(),
                        "${requireContext().getString(R.string.notLoggedIn)}",
                        Toast.LENGTH_LONG
                    )
                    toast.show()

                }

            }

        }

    }

}