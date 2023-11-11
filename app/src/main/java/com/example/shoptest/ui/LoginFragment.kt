package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        //View(Divider)
        val view = requireActivity().findViewById<View>(R.id.view)
        view.visibility = View.INVISIBLE


        //Toolbar
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.visibility = View.VISIBLE

        //Text der TextViews
        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)
        titleTextView.text = ("")

        //Sichtbarkeit ausschalten:
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE


        binding.iconIV.setImageResource(R.drawable.app_icon2)

        //Login
        binding.loginBTN.setOnClickListener {
            val email = binding.loginMailET.text.toString()
            val password = binding.loginPwET.text.toString()
            viewmodel.signIn(email, password)

        }

        //implement user
        viewmodel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().popBackStack()
                findNavController().navigate(R.id.homeFragment)
            }
        }

        //register
        binding.registerTV.setOnClickListener {
            it.findNavController().popBackStack()
            it.findNavController().navigate(R.id.registerFragment2)
        }
    }

}