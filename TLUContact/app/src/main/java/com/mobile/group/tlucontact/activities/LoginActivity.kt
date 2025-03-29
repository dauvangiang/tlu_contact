package com.mobile.group.tlucontact.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mobile.group.tlucontact.R

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_login)

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // Ánh xạ view
        edtEmail = findViewById(R.id.edt_email_register)
        edtPassword = findViewById(R.id.edt_password_register)
        btnLogin = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener { loginUser() }

        // Bắt sự kiện khi nhấn "Đăng ký"
        val tvRegister: TextView = findViewById(R.id.tv_register)
        tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterFinalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    if (user?.isEmailVerified == true) {
                        startActivity(Intent(this, UnitActivity::class.java))
                    } else {
                        Toast.makeText(this, "Vui lòng xác nhận email của bạn!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
