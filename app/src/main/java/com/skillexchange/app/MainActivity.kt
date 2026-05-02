package com.skillexchange.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.skillexchange.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Use custom navigation options for page shifting animations
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.fade_out)
                .setPopUpTo(navController.graph.startDestinationId, false, true)
                .build()

            navController.navigate(item.itemId, null, options)
            true
        }
        
        // Keep the selected tab highlighted when back stack changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.menu.findItem(destination.id)?.isChecked = true
        }

        // Animate bottom nav on show
        binding.bottomNavigation.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(400)
            .start()

        // Hide bottom nav on chat screen
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.chatFragment -> {
                    binding.bottomNavigation.animate()
                        .translationY(binding.bottomNavigation.height.toFloat())
                        .setDuration(250).start()
                }
                else -> {
                    binding.bottomNavigation.animate()
                        .translationY(0f)
                        .setDuration(250).start()
                }
            }
        }
    }
}
