package com.hn_2452.shoes_nike.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.ui.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DURATION: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay và chuyển đến WelcomeActivity
        Handler().postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish() // Đóng SplashActivity để ngăn ngừa quay lại
        }, SPLASH_DURATION)
    }
}