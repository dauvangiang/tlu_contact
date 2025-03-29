package com.mobile.group.tlucontact.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mobile.group.tlucontact.R

class RegisterFinalActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnRegister: Button

    private var fullName: String = ""
    private var email: String = ""
    private var idStaff: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_register_next)

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")

        // Nhận dữ liệu từ Intent
        fullName = intent.getStringExtra("FULL_NAME") ?: ""
        email = intent.getStringExtra("EMAIL") ?: ""
        idStaff = intent.getStringExtra("ID_STAFF") ?: ""

        // Ánh xạ view
        edtPassword = findViewById(R.id.new_password)
        edtConfirmPassword = findViewById(R.id.confirm_password)
        btnRegister = findViewById(R.id.btn_next_register)

        // Xử lý đăng ký
        btnRegister.setOnClickListener { registerUser() }

        // Bắt sự kiện khi nhấn "Đăng nhập"
        findViewById<TextView>(R.id.tv_login).setOnClickListener {
            startActivity(Intent(this@RegisterFinalActivity, LoginActivity::class.java))
        }
    }

    private fun registerUser() {
        val password = edtPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show()
            return
        }

        // Đăng ký tài khoản với Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verifyTask ->
                            if (verifyTask.isSuccessful) {
                                Toast.makeText(this, "Email xác nhận đã được gửi!", Toast.LENGTH_LONG).show()
                                saveUserToDatabase(user.uid)
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            } else {
                                val errorMessage = verifyTask.exception?.message ?: "Lỗi không xác định"
                                Toast.makeText(this, "Lỗi gửi email: $errorMessage", Toast.LENGTH_LONG).show()
                                Log.e("FirebaseAuth", "Lỗi gửi email: $errorMessage")
                            }
                        }
                } else {
                    val errorMessage = task.exception?.message ?: "Đăng ký thất bại"
                    Toast.makeText(this, "Đăng ký thất bại: $errorMessage", Toast.LENGTH_LONG).show()
                    Log.e("FirebaseAuth", "Đăng ký thất bại: $errorMessage")
                }
            }
    }

    private fun saveUserToDatabase(uid: String) {
        val userMap = mapOf(
            "fullName" to fullName,
            "email" to email,
            "idStaff" to idStaff
        )
        mDatabase.child(uid).setValue(userMap)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FirebaseDB", "Lỗi lưu dữ liệu: ${task.exception?.message}")
                }
            }
    }
}
