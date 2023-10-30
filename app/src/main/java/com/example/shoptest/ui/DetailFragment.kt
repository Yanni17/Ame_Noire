package com.example.shoptest.ui

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Im Zielfragment
        val inflater = TransitionInflater.from(context)
        val transition = inflater.inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition

    }


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

        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)

        // Ändere den Text des TextViews
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

                    val message = requireContext().getString(R.string.erfolgreichWarenkorb)
                    val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)

                    snackbar.view.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    snackbar.setBackgroundTint(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey
                        )
                    )
                    val textView =
                        snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(requireContext().getColor(R.color.text))

                    snackbar.show()

                    it.findNavController().navigateUp()


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
                        "Sie sind nicht angemeldet.",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }


            }

        }

    }

}