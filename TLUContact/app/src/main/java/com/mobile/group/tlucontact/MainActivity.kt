package com.mobile.group.tlucontact

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navItemSelected()
    }

    private fun navItemSelected() {
        val navView = findViewById<NavigationView>(R.id.sidebar_nav) ?: return
        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true

            // Thay đổi fragment (xử lý sau)

            true
        }
    }
}