package com.mobile.group.tlucontact.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mobile.group.tlucontact.R

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var edtEmail: EditText
    private lateinit var btnResetPassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        mAuth = FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.email)
        btnResetPassword = findViewById(R.id.btn_reset_password)

        btnResetPassword.setOnClickListener {
            val email = edtEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sendPasswordResetEmail(email)
        }

        val Login : TextView = findViewById(R.id.tv_login)
        Login.setOnClickListener {
            val intent = Intent(this@ResetPasswordActivity, AuthActivity::class.java)
            startActivity(intent)
        }

        updateUIVisibility(resources.configuration.orientation)
    }

    private fun sendPasswordResetEmail(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email đặt lại mật khẩu đã được gửi!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Lỗi khi gửi email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        updateUIVisibility(newConfig.orientation)
    }

    private fun updateUIVisibility(orientation: Int) {
        if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // Android 11+
                window.insetsController?.let {
                    it.hide(android.view.WindowInsets.Type.systemBars())
                    it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else { // Dưới Android 11
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                                View.SYSTEM_UI_FLAG_FULLSCREEN
                        )
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // Android 11+
                window.insetsController?.show(android.view.WindowInsets.Type.systemBars())
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }
}
