package com.example.shoptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.shoptest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        val toolbar: Toolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(toolbar)
        setToolbarTitle("Home")
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.popBackStack(R.id.homeFragment, false)
                    false
                }

                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment)
                    true // true, um das Element auszuwählen
                }

                R.id.favoriteFragment -> {
                    navController.navigate(R.id.favoriteFragment)
                    true
                }

                else -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                }
            }
        }

    }

    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }
}