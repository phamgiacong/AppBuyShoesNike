package com.hn_2452.shoes_nike.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.WELCOME_KEY
import com.hn_2452.shoes_nike.utility.saveBooleanDataByKey

class WelcomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: WelcomePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        viewPager = findViewById(R.id.viewPager)
        pagerAdapter = WelcomePagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
    }

    fun nextAction() {
        viewPager.currentItem += 1
    }

    fun startAppAction() {
        saveBooleanDataByKey(this, WELCOME_KEY, true)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}