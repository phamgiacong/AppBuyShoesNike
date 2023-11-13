package com.hn_2452.shoes_nike.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.R

class WelcomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: WelcomePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            nextPage()
        }
        val getStartedButton = findViewById<Button>(R.id.getStartedButton)
        getStartedButton.setOnClickListener {
            getStarted()
        }
        viewPager = findViewById(R.id.viewPager)
        pagerAdapter = WelcomePagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
    }

    // Phương thức để chuyển đến màn hình tiếp theo
    fun nextPage() {
        val nextPage = viewPager.currentItem + 1
        if (nextPage < pagerAdapter.count) {
            viewPager.currentItem = nextPage
        } else {
        // Đã đến trang cuối cùng, thực hiện các hành động tùy chọn
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Phương thức để chuyển sang MainActivity khi nhấn nút "Get Started"
    fun getStarted() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Đóng WelcomeActivity nếu không muốn quay lại nó từ MainActivity
    }
}