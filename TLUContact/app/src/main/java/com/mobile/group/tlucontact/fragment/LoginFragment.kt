package com.mobile.group.tlucontact.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.google.firebase.auth.FirebaseAuth
import com.mobile.group.tlucontact.MainActivity
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.activities.ResetPasswordActivity

class LoginFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnForgotPassword: TextView
    private lateinit var  sharedPreferences: SharedPreferences
    private lateinit var rememberMe : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        getReferenceToViews(view)
        setListeners()
        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE)
    }

    private fun setListeners() {
        btnLogin.setOnClickListener { loginUser() }
        rememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                clearCredentials()
            }
        }
        btnForgotPassword.setOnClickListener {
            startActivity(Intent(requireContext(), ResetPasswordActivity::class.java))
        }
    }

    private fun getReferenceToViews(view: View) {
        edtEmail = view.findViewById(R.id.email)
        edtPassword = view.findViewById(R.id.password)
        btnLogin = view.findViewById(R.id.btn_login)
        rememberMe = view.findViewById(R.id.remember_me)
        btnForgotPassword = view.findViewById(R.id.forgot_password)
    }

    private fun loginUser() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    if (user?.isEmailVerified == true) {
                        // Lưu thông tin ngay khi đăng nhập thành công
                        if (rememberMe.isChecked) {
                            saveCredentials(email, password)
                        } else {
                            clearCredentials()
                        }

                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "Vui lòng xác nhận email của bạn!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Đăng nhập thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
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