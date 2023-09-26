package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.databinding.FragmentDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        val mainActivity = activity as MainActivity
        mainActivity.setToolbarTitle("Produkt Detail")

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Um die Sichtbarkeit anszuschalten:
        bottomNavigationView.visibility = View.VISIBLE

        var id = requireArguments().getInt("id")


        viewModel.getDetail(id).observe(viewLifecycleOwner) {

            with(binding) {
                imageView4.load(it.image)
                preis4TV.text = String.format("%.2f", it.price) + " €"
                descriptionTV2.text = it.description
                title4TV.text = it.title
                ratingBTN2.setImageResource(R.drawable.baseline_star_24)
                ratingTV2.text = it.rating.rate.toString()
                if (it.isLiked) {
                    likeBTN2.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    likeBTN2.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }
    }

}