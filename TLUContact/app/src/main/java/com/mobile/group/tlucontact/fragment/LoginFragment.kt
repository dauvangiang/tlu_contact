package com.mobile.group.tlucontact.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.mobile.group.tlucontact.MainActivity
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.activities.ResetPasswordActivity

//import com.mobile.group.tlucontact.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnForgotPassword: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rememberMe: CheckBox
    private lateinit var progressDialog: ProgressDialog
    private lateinit var passwordToggle: ImageView
    private var isPasswordVisible = false

    //private val loginViewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        getReferenceToViews(view)
        setListeners()
        logSharedPreferences()  // In dữ liệu ra Logcat
        // Khôi phục dữ liệu từ SharedPreferences vào ViewModel
        //loadSavedCredentials()

        // Quan sát ViewModel và cập nhật UI
        //observeViewModel()

        progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Đang đăng nhập...")
            setCancelable(false)
        }
    }

//    private fun observeViewModel() {
//        loginViewModel.email.observe(viewLifecycleOwner) { email ->
//            if (edtEmail.text.toString() != email) {
//                edtEmail.setText(email)
//            }
//        }
//
//        loginViewModel.password.observe(viewLifecycleOwner) { password ->
//            if (edtPassword.text.toString() != password) {
//                edtPassword.setText(password)
//            }
//        }
//
//        loginViewModel.rememberMe.observe(viewLifecycleOwner) { remember ->
//            rememberMe.isChecked = remember
//        }
//    }

    private fun setListeners() {
        btnLogin.setOnClickListener { loginUser() }
        passwordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                edtPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_visibility_off) // biểu tượng đóng mắt
            } else {
                edtPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_visibility) // biểu tượng mở mắt
            }

            // Đặt lại con trỏ về cuối chuỗi
            edtPassword.setSelection(edtPassword.text.length)
        }


//        // Update ViewModel when user enters email
//        edtEmail.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                loginViewModel.setEmail(s.toString())
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//
//        // Update ViewModel when user enters password
//        edtPassword.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                loginViewModel.setPassword(s.toString())
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//
//        // Update "Remember Me" when checked
//        rememberMe.setOnCheckedChangeListener { _, isChecked ->
//            loginViewModel.setRememberMe(isChecked)
//        }

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
        passwordToggle = view.findViewById(R.id.password_toggle)
    }

    private fun loginUser() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show()
            return
        }

        progressDialog.show()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        progressDialog.dismiss()
                        if (tokenTask.isSuccessful) {
                            val token = tokenTask.result?.token
                            if (!token.isNullOrEmpty()) {
                                saveToken(token)
                            }

                            if (user.isEmailVerified) {
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
                            Toast.makeText(requireContext(), "Lỗi khi lấy token: ${tokenTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Đăng nhập thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }



    private fun saveCredentials(email: String, password: String) {
        sharedPreferences.edit().apply {
            putString("email", email)
            putString("password", password)
            putBoolean("remember", true)
            apply()
        }
    }

    private fun clearCredentials() {
        sharedPreferences.edit().apply {
            remove("email")
            remove("password")
            putBoolean("remember", false)
            apply()
        }
    }

//    private fun loadSavedCredentials() {
//        val isRemembered = sharedPreferences.getBoolean("remember", false)
//        if (isRemembered) {
//            val savedEmail = sharedPreferences.getString("email", "") ?: ""
//            val savedPassword = sharedPreferences.getString("password", "") ?: ""
//            loginViewModel.setEmail(savedEmail)
//            loginViewModel.setPassword(savedPassword)
//            loginViewModel.setRememberMe(true)
//        }
//    }
    private fun saveToken(token: String) {
        sharedPreferences.edit().apply {
            putString("auth_token", token)
            apply()
        }
    }
    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }
    private fun logSharedPreferences() {
        val email = sharedPreferences.getString("email", "Không có dữ liệu")
        val password = sharedPreferences.getString("password", "Không có dữ liệu")
        val remember = sharedPreferences.getBoolean("remember", false)
        val token = sharedPreferences.getString("auth_token", "Không có token")

        android.util.Log.d("SharedPreferences", "Email: $email")
        android.util.Log.d("SharedPreferences", "Password: $password")
        android.util.Log.d("SharedPreferences", "Remember Me: $remember")
        android.util.Log.d("SharedPreferences", "Auth Token: $token")
    }

}
