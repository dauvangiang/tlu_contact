package com.mobile.group.tlucontact.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mobile.group.tlucontact.R

class RegisterFragment : Fragment() {
    private lateinit var edtEmail: EditText
    private lateinit var edtCode: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("users")
        getRefenceToViews(view)
        setListener()
    }

    private fun setListener() {
        btnRegister.setOnClickListener { registerUser() }
    }

    private fun getRefenceToViews(view: View) {
        edtEmail = view.findViewById(R.id.email)
        edtCode = view.findViewById(R.id.code)
        edtPassword = view.findViewById(R.id.password)
        edtConfirmPassword = view.findViewById(R.id.confirm_password)
        btnRegister = view.findViewById(R.id.btn_register)
    }

    private fun registerUser() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show()
            return
        }

        // Đăng ký tài khoản với Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verifyTask ->
                            if (verifyTask.isSuccessful) {
                                Toast.makeText(requireContext(), "Email xác nhận đã được gửi!", Toast.LENGTH_LONG).show()
                                saveUserToDatabase(user.uid, email, edtCode.text.toString())
                            } else {
                                val errorMessage = verifyTask.exception?.message ?: "Lỗi không xác định"
                                Toast.makeText(requireContext(), "Lỗi gửi email: $errorMessage", Toast.LENGTH_LONG).show()
                                Log.e("FirebaseAuth", "Lỗi gửi email: $errorMessage")
                            }
                        }
                } else {
                    val errorMessage = task.exception?.message ?: "Đăng ký thất bại"
                    Toast.makeText(requireContext(), "Đăng ký thất bại: $errorMessage", Toast.LENGTH_LONG).show()
                    Log.e("FirebaseAuth", "Đăng ký thất bại: $errorMessage")
                }
            }
    }

    private fun saveUserToDatabase(uid: String, email: String, code: String) {
        val userMap = mapOf(
            "email" to email,
            "code" to code
        )
        mDatabase.child(uid).setValue(userMap)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FirebaseDB", "Lỗi lưu dữ liệu: ${task.exception?.message}")
                }
            }
    }
}