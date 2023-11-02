
package com.example.shoptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
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


        navController.currentBackStack.value.find { it.destination.id == R.id.startFragment }?.let {
            Log.d("IDS", "${it.destination.id},${R.id.startFragment}")
            navController.popBackStack()
        }


        binding.bottomNavigationView.setupWithNavController(navController)

        val toolbar: Toolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            Log.d(
                "Stack",
                navController.currentBackStack.value.joinToString { it.destination.displayName + "\n" })

            if (navController.currentDestination!!.id != item.itemId) {
                navController.navigate(item.itemId)
            }
            false

        }
    }
}

