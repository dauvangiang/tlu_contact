package com.mobile.group.tlucontact

import android.os.Bundle
import android.widget.Toast
import com.mobile.group.tlucontact.fragment.ContactListFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.mobile.group.tlucontact.databinding.ActivityMainBinding
import com.mobile.group.tlucontact.fragment.DepartmentListFragment
import com.mobile.group.tlucontact.fragment.StaffListFragment
import com.mobile.group.tlucontact.fragment.StudentListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Default to Staff fragment when app starts
            loadFragment(DepartmentListFragment())
            binding?.bottomNav?.selectedItemId = R.id.nav_departments_contact

        }

        setupBottomNavigation()
        navItemSelected()
    }

    private fun setupBottomNavigation() {
        binding.bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_departments_contact -> {
                    loadFragment(DepartmentListFragment())
                    true
                }
                R.id.nav_staffs_contact -> {
                    loadFragment(StaffListFragment())
                    true
                }
                R.id.nav_students_contact -> {
                    loadFragment(StudentListFragment())
                    true
                }
                else -> false
            }
        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
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
