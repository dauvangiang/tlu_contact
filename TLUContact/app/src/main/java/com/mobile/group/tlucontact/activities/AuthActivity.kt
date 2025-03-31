package com.mobile.group.tlucontact.activities

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.fragment.LoginFragment
import com.mobile.group.tlucontact.fragment.RegisterFragment

class AuthActivity : AppCompatActivity() {
    private lateinit var tvLogin: TextView
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        getReferenceToViews()
        setListener()

        loadFragment(LoginFragment())

        onBackPressedDispatcher.addCallback(this, backPressedCallback)
        updateUIVisibility(resources.configuration.orientation)
    }

    private fun loadFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    private fun getReferenceToViews() {
        tvLogin = findViewById(R.id.tv_login)
        tvRegister = findViewById(R.id.tv_register)
    }

    private fun setListener() {
        tvLogin.setOnClickListener { openLoginFragment() }
        tvRegister.setOnClickListener { openRegisterFragment() }
    }

    private fun openLoginFragment() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        setLoginSelected()
    }

    private fun openRegisterFragment() {
        loadFragment(RegisterFragment(), addToBackStack = true)
        setRegisterSelected()
    }

    private fun setLoginSelected() {
        setTextViewState(tvLogin, R.color.primary_blue, Typeface.BOLD)
        setTextViewState(tvRegister, R.color.second_black, Typeface.NORMAL)
    }

    private fun setRegisterSelected() {
        setTextViewState(tvLogin, R.color.second_black, Typeface.NORMAL)
        setTextViewState(tvRegister, R.color.primary_blue, Typeface.BOLD)
    }

    private fun setTextViewState(view: TextView, color: Int, typeface: Int) {
        view.setTextColor(ContextCompat.getColor(this, color))
        view.setTypeface(null, typeface)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                setLoginSelected()
            } else {
                finish()
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