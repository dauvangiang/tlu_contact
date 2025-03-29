package com.mobile.group.tlucontact.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.group.tlucontact.R


class RegisterActivity : AppCompatActivity() {



    private lateinit var edtFullName: EditText
    private lateinit var edtEmail: EditText

    private lateinit var btnRegisterNext: Button
    private lateinit var edtIdstaff: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_register)



        // Ánh xạ
        edtEmail = findViewById(R.id.edt_email_register)
        edtFullName = findViewById(R.id.edt_full_name)
        edtIdstaff = findViewById(R.id.edt_id_staff)
        btnRegisterNext = findViewById(R.id.btn_next_register)

        // Xử lý đăng ký
        btnRegisterNext.setOnClickListener {
            val fullName = edtFullName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val idStaff = edtIdstaff.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || idStaff.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tạo Intent để chuyển sang RegisterFinalActivity
            val intent = Intent(this@RegisterActivity, RegisterFinalActivity::class.java)

            // Truyền dữ liệu sang RegisterFinalActivity
            intent.putExtra("FULL_NAME", fullName)
            intent.putExtra("EMAIL", email)
            intent.putExtra("ID_STAFF", idStaff)

            // Chuyển sang RegisterFinalActivity
            startActivity(intent)
        }


        // Bắt sự kiện khi nhấn "Đăng nhập"
        val tvLogin: TextView = findViewById(R.id.tv_login)
        tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}


