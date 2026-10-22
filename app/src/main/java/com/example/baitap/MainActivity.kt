package com.example.baitap   // đổi thành package của bạn

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged

class MainActivity : AppCompatActivity() {

    /** Mỗi trường gồm: ô nhập, TextView báo lỗi, hàm kiểm tra (null = hợp lệ) */
    private class Field(
        val edt: EditText,
        val tvError: TextView,
        val check: (String) -> String?
    )

    private lateinit var fields: List<Field>

    private val required = "Vui lòng điền vào trường này."

    // ---------- Các hàm validation ----------

    private fun checkName(s: String): String? = when {
        s.isEmpty() -> required
        !s.matches(Regex("^[\\p{L} ]+$")) ->
            "Họ tên chỉ được chứa chữ cái và khoảng trắng."
        s.split(Regex("\\s+")).size < 2 -> "Vui lòng nhập đầy đủ họ và tên."
        else -> null
    }

    private fun checkEmail(s: String): String? = when {
        s.isEmpty() -> required
        !Patterns.EMAIL_ADDRESS.matcher(s).matches() -> "Email không hợp lệ."
        else -> null
    }

    // SĐT Việt Nam: 0xxxxxxxxx hoặc +84xxxxxxxxx (đầu số 3,5,7,8,9)
    private fun checkPhone(s: String): String? = when {
        s.isEmpty() -> required
        !s.matches(Regex("^(0|\\+84)(3|5|7|8|9)\\d{8}$")) ->
            "Số điện thoại không hợp lệ (VD: 0912345678)."
        else -> null
    }

    // MSV: chữ và số, 6-15 ký tự. Sửa regex nếu trường bạn có quy tắc khác
    private fun checkStudentId(s: String): String? = when {
        s.isEmpty() -> required
        !s.matches(Regex("^[A-Za-z0-9]{6,15}$")) ->
            "MSV gồm 6-15 ký tự chữ và số, không có khoảng trắng."
        else -> null
    }

    // Lớp: chữ, số, dấu gạch ngang/gạch dưới/chấm (VD: D20CQCN01-B)
    private fun checkClass(s: String): String? = when {
        s.isEmpty() -> required
        !s.matches(Regex("^[\\p{L}0-9][\\p{L}0-9 ._-]{1,29}$")) ->
            "Tên lớp không hợp lệ (VD: D20CQCN01-B)."
        else -> null
    }

    // ---------- Vòng đời ----------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fields = listOf(
            Field(findViewById(R.id.edtName), findViewById(R.id.tvNameError), ::checkName),
            Field(findViewById(R.id.edtEmail), findViewById(R.id.tvEmailError), ::checkEmail),
            Field(findViewById(R.id.edtPhone), findViewById(R.id.tvPhoneError), ::checkPhone),
            Field(findViewById(R.id.edtStudentId), findViewById(R.id.tvStudentIdError), ::checkStudentId),
            Field(findViewById(R.id.edtClass), findViewById(R.id.tvClassError), ::checkClass)
        )

        // Kiểm tra khi đang gõ và khi rời khỏi ô nhập
        fields.forEach { f ->
            f.edt.doAfterTextChanged { validate(f) }
            f.edt.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validate(f) }
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener { onRegister() }

        // Nút gọi Intent sang màn hình Chi tiết (không kèm dữ liệu)
        findViewById<Button>(R.id.btnGoDetail).setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java))
        }
    }

    // ---------- Xử lý ----------

    /** Cập nhật viền + thông báo lỗi. Trả về true nếu hợp lệ */
    private fun validate(f: Field): Boolean {
        val error = f.check(f.edt.text.toString().trim())
        if (error == null) {
            f.edt.setBackgroundResource(R.drawable.bg_input_success)
            f.tvError.visibility = View.GONE
        } else {
            f.edt.setBackgroundResource(R.drawable.bg_input_error)
            f.tvError.text = error
            f.tvError.visibility = View.VISIBLE
        }
        return error == null
    }

    private fun onRegister() {
        // map{} chạy hết cả 5 trường (không dừng ở lỗi đầu tiên) nên ô nào lỗi cũng hiện thông báo
        val allValid = fields.map { validate(it) }.all { it }
        if (!allValid) return

        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_NAME, fields[0].edt.text.toString().trim())
            putExtra(DetailActivity.EXTRA_EMAIL, fields[1].edt.text.toString().trim())
            putExtra(DetailActivity.EXTRA_PHONE, fields[2].edt.text.toString().trim())
            putExtra(DetailActivity.EXTRA_STUDENT_ID, fields[3].edt.text.toString().trim())
            putExtra(DetailActivity.EXTRA_CLASS, fields[4].edt.text.toString().trim())
        }
        startActivity(intent)
    }
}