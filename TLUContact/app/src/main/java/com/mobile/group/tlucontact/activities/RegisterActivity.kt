package com.mobile.group.tlucontact.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.group.tlucontact.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var edtFullName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtIdstaff: EditText
    private lateinit var btnRegisterNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_register)

        // Ánh xạ view
        edtEmail = findViewById(R.id.email)
        edtFullName = findViewById(R.id.full_name)
        edtIdstaff = findViewById(R.id.id_person)
        btnRegisterNext = findViewById(R.id.btn_next_register)

        // Xử lý khi nhấn nút "Tiếp theo"
        btnRegisterNext.setOnClickListener {
            val fullName = edtFullName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val idStaff = edtIdstaff.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || idStaff.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Truyền dữ liệu sang RegisterFinalActivity
            val intent = Intent(this@RegisterActivity, RegisterFinalActivity::class.java).apply {
                putExtra("FULL_NAME", fullName)
                putExtra("EMAIL", email)
                putExtra("ID_STAFF", idStaff)
            }
            startActivity(intent)
        }

        // Bắt sự kiện khi nhấn "Đăng nhập"
        findViewById<TextView>(R.id.tv_login).setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }
}
