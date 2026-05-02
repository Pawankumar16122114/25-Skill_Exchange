package com.skillexchange.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.skillexchange.app.auth.AuthActivity
import com.skillexchange.app.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animate logo elements
        binding.ivLogo.alpha = 0f
        binding.tvAppName.alpha = 0f
        binding.tvTagline.alpha = 0f

        binding.ivLogo.animate()
            .alpha(1f).scaleX(1f).scaleY(1f)
            .setDuration(600).setStartDelay(200).start()

        binding.tvAppName.animate()
            .alpha(1f).translationY(0f)
            .setDuration(500).setStartDelay(500).start()

        binding.tvTagline.animate()
            .alpha(1f).translationY(0f)
            .setDuration(500).setStartDelay(700)
            .withEndAction {
                // Check auth state after animations
                android.os.Handler(mainLooper).postDelayed({
                    navigateNext()
                }, 600)
            }.start()
    }

    private fun navigateNext() {
        val intent = if (FirebaseAuth.getInstance().currentUser != null) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, AuthActivity::class.java)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
