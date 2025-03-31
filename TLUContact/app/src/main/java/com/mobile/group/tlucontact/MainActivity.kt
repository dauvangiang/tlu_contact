package com.mobile.group.tlucontact

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.mobile.group.tlucontact.databinding.ActivityMainBinding
import com.mobile.group.tlucontact.fragment.DepartmentListFragment
import com.mobile.group.tlucontact.fragment.StaffListFragment
import com.mobile.group.tlucontact.fragment.StudentListFragment
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.mobile.group.tlucontact.fragment.StaffProfileFragment
import com.mobile.group.tlucontact.fragment.StudentProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isLandscape = false
    private val userRole = "STUDENT" // Tạm thời hardcode, sau này sẽ lấy từ login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        remoteConfig()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the device is in landscape mode
        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (savedInstanceState == null) {
            if (isLandscape) {
                when (userRole) {
                    "STAFF" -> loadFragment(StaffProfileFragment())
                    "STUDENT" -> loadFragment(StudentProfileFragment())
                    else -> loadFragment(DepartmentListFragment())
                }
            } else {
                loadFragment(DepartmentListFragment())
                binding.bottomNav?.selectedItemId = R.id.nav_departments_contact
            }
        }

        if (isLandscape) {
            setupSidebarNavigation()
        } else {
            setupBottomNavigation()
        }
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
                R.id.nav_about -> {
                    // Kiểm tra role và load fragment tương ứng
                    when (userRole) {
                        "STAFF" -> loadFragment(StaffProfileFragment())
                        "STUDENT" -> loadFragment(StudentProfileFragment())
                        else -> {
                            Toast.makeText(this, "Không xác định được vai trò người dùng", Toast.LENGTH_SHORT).show()
                            false
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setupSidebarNavigation() {
        val navView = findViewById<NavigationView>(R.id.sidebar_nav) ?: return
        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true

            when (menuItem.itemId) {
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
                R.id.nav_about -> {
                    when (userRole) {
                        "STAFF" -> loadFragment(StaffProfileFragment())
                        "STUDENT" -> loadFragment(StudentProfileFragment())
                        else -> {
                            Toast.makeText(this, "Không xác định được vai trò người dùng", Toast.LENGTH_SHORT).show()
                            false
                        }
                    }
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

    private fun remoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val config = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(config)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
    }
}