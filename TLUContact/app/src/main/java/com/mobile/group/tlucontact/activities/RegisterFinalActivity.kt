//package com.mobile.group.tlucontact.activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.mobile.group.tlucontact.R
//
//
//class RegisterFinalActivity : AppCompatActivity() {
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var mDatabase: DatabaseReference
//
//    private lateinit var edtPassword: EditText
//    private lateinit var edtConfirmPassword: EditText
//    private lateinit var btnRegister: Button
//
//    private lateinit var fullName: String
//    private lateinit var email: String
//    private lateinit var idStaff: String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.frame_register_next)
//
//        // Nhận dữ liệu từ RegisterActivity
//        fullName = intent.getStringExtra("FULL_NAME") ?: ""
//
//        idStaff = intent.getStringExtra("ID_STAFF") ?: ""
//
//        // Khởi tạo FirebaseAuth
//         mAuth = FirebaseAuth.getInstance()
//        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
//
//        // Ánh xạ
//        edtPassword = findViewById(R.id.edt_password_register)
//        edtConfirmPassword = findViewById(R.id.edt_confirm_password_register)
//        btnRegister = findViewById(R.id.btn_register)
//
//        // Xử lý đăng ký
//        btnRegister.setOnClickListener { registerUser() }
//
//        // Bắt sự kiện khi nhấn "Đăng nhập"
//        val tvLogin: TextView = findViewById(R.id.tv_login)
//        tvLogin.setOnClickListener {
//            val intent = Intent(this@RegisterFinalActivity, LoginActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//    private fun registerUser() {
//        val password = edtPassword.text.toString().trim()
//        val confirmPassword = edtConfirmPassword.text.toString().trim()
//        var  email = intent.getStringExtra("EMAIL") ?: ""
//        if (password.isEmpty() || confirmPassword.isEmpty()) {
//            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (password != confirmPassword) {
//            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        // Đăng ký tài khoản với Firebase Auth
//        mAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = mAuth.currentUser
//                    user?.sendEmailVerification()
//                        ?.addOnCompleteListener { verifyTask ->
//                            if (verifyTask.isSuccessful) {
//                                Toast.makeText(this, "Email xác nhận đã được gửi!", Toast.LENGTH_LONG).show()
//                            } else {
//                                val errorMessage = verifyTask.exception?.message ?: "Lỗi không xác định"
//                                Toast.makeText(this, "Lỗi gửi email: $errorMessage", Toast.LENGTH_LONG).show()
//                                android.util.Log.e("FirebaseAuth", "Lỗi gửi email: $errorMessage")
//                            }
//                        }
//                } else {
//                    val errorMessage = task.exception?.message ?: "Đăng ký thất bại"
//                    Toast.makeText(this, "Đăng ký thất bại: $errorMessage", Toast.LENGTH_SHORT).show()
//                    android.util.Log.e("FirebaseAuth", "Đăng ký thất bại: $errorMessage")
//                }
//            }
//
//
//
//    }
//
//
//}
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
    private lateinit var edtEmail: EditText
    private lateinit var edtFullName: EditText
    private lateinit var edtIdStaff: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_register_next)

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")

        // Ánh xạ view
        edtPassword = findViewById(R.id.edt_password_register)
        edtConfirmPassword = findViewById(R.id.edt_confirm_password_register)
        btnRegister = findViewById(R.id.btn_register)
        edtEmail = findViewById(R.id.edt_email_register)
        edtFullName = findViewById(R.id.edt_full_name)
        edtIdStaff = findViewById(R.id.edt_id_staff)

        // Xử lý đăng ký
        btnRegister.setOnClickListener { registerUser() }

        // Bắt sự kiện khi nhấn "Đăng nhập"
        findViewById<TextView>(R.id.tv_login).setOnClickListener {
            startActivity(Intent(this@RegisterFinalActivity, LoginActivity::class.java))
        }
    }

    private fun registerUser() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()
        val fullName = edtFullName.text.toString().trim()
        val idStaff = edtIdStaff.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || idStaff.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
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
                                saveUserToDatabase(user.uid, fullName, email, idStaff)
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

    private fun saveUserToDatabase(uid: String, fullName: String, email: String, idStaff: String) {
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

