package com.mobile.group.tlucontact.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mobile.group.tlucontact.R

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var togglePasswordVisibility: ImageView
    private var isPasswordVisible = false
    private lateinit var  sharedPreferences: SharedPreferences;
    private lateinit var rememberMe : CheckBox;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_login)

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // Ánh xạ view
        edtEmail = findViewById(R.id.email)
        edtPassword = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener { loginUser() }
        edtPassword = findViewById(R.id.password)
        togglePasswordVisibility = findViewById(R.id.toggle_password_visibility)

        rememberMe = findViewById(R.id.remember_me)
        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        loadSavedCredentials()
        rememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                clearCredentials()
            }
        }
        togglePasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off)
            } else {
                edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility)
            }
            // Giữ con trỏ ở cuối sau khi thay đổi kiểu nhập
            edtPassword.setSelection(edtPassword.text.length)
        }
        // Bắt sự kiện khi nhấn "Đăng ký"
        val tvRegister: TextView = findViewById(R.id.tv_register)
        tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        val ResetPassword : TextView = findViewById(R.id.forgot_password)
        ResetPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
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
                        // Lưu thông tin ngay khi đăng nhập thành công
                        if (rememberMe.isChecked) {
                            saveCredentials(email, password)
                        } else {
                            clearCredentials()
                        }

                        startActivity(Intent(this, UnitActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Vui lòng xác nhận email của bạn!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loadSavedCredentials() {
        val savedEmail = sharedPreferences.getString("email", "")
        val savedPassword = sharedPreferences.getString("password", "")
        val isChecked = sharedPreferences.getBoolean("remember", false)

        edtEmail.setText(savedEmail)
        edtPassword.setText(savedPassword)
        rememberMe.isChecked = isChecked
    }

    private fun saveCredentials(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putBoolean("remember", true)
        editor.apply()
    }

    private fun clearCredentials() {
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.putBoolean("remember", false)
        editor.apply()
    }

}