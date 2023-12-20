package com.hn_2452.shoes_nike.ui.splash_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.WELCOME_KEY
import com.hn_2452.shoes_nike.ui.welcome.WelcomeActivity
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.getBooleanDataByKey
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    companion object {
        const val TAG = "Nike:SplashActivity: "
    }

    private val mSplashActivityViewModel: SplashActivityViewModel by viewModels()

    private val SPLASH_DURATION: Long = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val token = getStringDataByKey(this, TOKEN)
        Log.i(TAG, "token: $token")
        if (token.isNotEmpty()) {
            mSplashActivityViewModel.autoLogin(token)
                .observe(this@SplashActivity) { result ->
                    when (result?.status) {
                        Status.LOADING -> {
                            Log.i(TAG, "autoLogin: loading...")
                        }

                        Status.SUCCESS -> {
                            Log.i(TAG, "autoLogin: success")
                            startMainActivity()
                        }

                        Status.ERROR -> {
                            Log.e(TAG, "autoLogin: ${result.message}")
                            startMainActivity()
                        }

                        null -> {
                            Log.e(TAG, "autoLogin: result is null")
                            startMainActivity()
                        }
                    }
                }
        } else {
            if (getBooleanDataByKey(this, WELCOME_KEY)) {
                startMainActivity()
            } else {
                // Delay và chuyển đến WelcomeActivity
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish() // Đóng SplashActivity để ngăn ngừa quay lại
                }, SPLASH_DURATION)
            }

        }
    }

    private fun startMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000L)
    }
}