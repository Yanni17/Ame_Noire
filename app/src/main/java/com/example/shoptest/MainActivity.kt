package com.example.shoptest

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.shoptest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController


        //no StartScreen
        navController.currentBackStack.value.find { it.destination.id == R.id.startFragment }?.let {
            navController.popBackStack()
        }

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            if (navController.currentDestination!!.id != item.itemId) {
                navController.navigate(item.itemId)
            }
            false

        }

        Log.d("yanni", "${viewModel.liveListOfCartItems.value}")

        viewModel.liveListOfCartItems.observe(this, Observer {

            Log.d("yanni2", "${viewModel.liveListOfCartItems.value}")
            var badge = binding.bottomNavigationView.getOrCreateBadge(R.id.cashoutFragment)

            if (it.sumOf { it.quantity } < 1) {
                badge.isVisible = false
            } else {
                badge.isVisible = true
                badge.number = it.sumOf { it.quantity }
                badge.backgroundColor = this.getColor(R.color.gold)

            }

        })
    }

}

