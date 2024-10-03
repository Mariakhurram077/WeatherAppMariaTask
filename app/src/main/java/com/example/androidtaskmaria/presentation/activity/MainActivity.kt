package com.example.androidtaskmaria.presentation.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.androidtaskmaria.R
import com.example.androidtaskmaria.databinding.ActivityMainBinding
import com.example.androidtaskmaria.presentation.getstarted.GetStartedFragment
import com.example.androidtaskmaria.presentation.homescreen.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
                val currentFragment = navHostFragment.childFragmentManager.fragments.firstOrNull()
                when (currentFragment) {
                    is HomeFragment -> {
                        moveTaskToBack(true)
                    }

                    is GetStartedFragment -> {
                        moveTaskToBack(true)
                    }

                    else -> {
                        findNavController(R.id.nav_host_fragment_container).navigateUp()
                    }
                }
            }
        })
    }
}