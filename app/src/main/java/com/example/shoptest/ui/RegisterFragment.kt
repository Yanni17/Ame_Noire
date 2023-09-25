package com.example.shoptest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    val viewmodel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentRegisterBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerBTN.setOnClickListener {
            val email = binding.emailET.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            val telefonNummer = binding.telefonNummerET.text.toString()
            val name = binding.nameET.text.toString()

            viewmodel.signUp(email,password,name,telefonNummer)
        }

        viewmodel.user.observe(viewLifecycleOwner){
            if (it != null ){
                findNavController().navigate(R.id.homeFragment)
            }
        }



    }



}