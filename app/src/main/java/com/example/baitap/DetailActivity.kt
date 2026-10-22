package com.example.baitap   // đổi thành package của bạn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_PHONE = "extra_phone"
        const val EXTRA_STUDENT_ID = "extra_student_id"
        const val EXTRA_CLASS = "extra_class"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val name = intent.getStringExtra(EXTRA_NAME)

        if (name == null) {
            // Mở trực tiếp từ nút "Xem màn hình Chi tiết", chưa có dữ liệu
            findViewById<View>(R.id.layoutInfo).visibility = View.GONE
            findViewById<View>(R.id.tvEmpty).visibility = View.VISIBLE
        } else {
            findViewById<TextView>(R.id.tvName).text = name
            findViewById<TextView>(R.id.tvEmail).text = intent.getStringExtra(EXTRA_EMAIL)
            findViewById<TextView>(R.id.tvPhone).text = intent.getStringExtra(EXTRA_PHONE)
            findViewById<TextView>(R.id.tvStudentId).text = intent.getStringExtra(EXTRA_STUDENT_ID)
            findViewById<TextView>(R.id.tvClass).text = intent.getStringExtra(EXTRA_CLASS)
        }

        // Nút gọi Intent quay về màn hình Đăng ký
        findViewById<Button>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                // Dùng lại màn hình Đăng ký đã có trong back stack, tránh tạo trùng
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }
    }
}