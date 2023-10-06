package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.databinding.FragmentLoginBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginFragment : Fragment() {

    val viewmodel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.setToolbarTitle("")
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE

        // Um die Sichtbarkeit auszuschalten:
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE

        // Icon setzen
        binding.iconIV.setImageResource(R.drawable.app_icon2)

        binding.loginBTN.setOnClickListener {
            val email = binding.loginMailET.text.toString()
            val password = binding.loginPwET.text.toString()
            viewmodel.signIn(email, password)
        }

        viewmodel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.homeFragment)
            }
        }

        binding.registerTV.setOnClickListener {
            it.findNavController().navigate(R.id.registerFragment2)
        }

    }

}