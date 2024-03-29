package com.hn_2452.shoes_nike.ui.splash_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.NEW_SHOES_NOTIFY
import com.hn_2452.shoes_nike.OFFER_NOTIFY
import com.hn_2452.shoes_nike.ORDER_NOTIFY
import com.hn_2452.shoes_nike.OTHER_NOTIFY
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.WELCOME_KEY
import com.hn_2452.shoes_nike.ui.welcome.WelcomeActivity
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.getBooleanDataByKey
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import com.hn_2452.shoes_nike.utility.saveBooleanDataByKey
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    companion object {
        const val TAG = "Nike:SplashActivity: "
    }

    private val mSplashActivityViewModel: SplashActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val receivedBundle = intent.extras
        Log.e("TAG", "onCreate: ${receivedBundle?.getString("type")}")
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
                        }

                        Status.ERROR -> {
                            Log.e(TAG, "autoLogin: ${result.message}")
                        }

                        null -> {
                            Log.e(TAG, "autoLogin: result is null")
                        }
                    }
                }
            startMainActivity(receivedBundle)
        } else {
            if (getBooleanDataByKey(this, WELCOME_KEY)) {
                startMainActivity(receivedBundle)
            } else {
                saveBooleanDataByKey(this, NEW_SHOES_NOTIFY, true)
                saveBooleanDataByKey(this, ORDER_NOTIFY, true)
                saveBooleanDataByKey(this, OFFER_NOTIFY, true)
                saveBooleanDataByKey(this, OTHER_NOTIFY, true)
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    private fun startMainActivity(bundle: Bundle?) {
        if (bundle == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }
    }
}