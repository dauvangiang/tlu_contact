package com.mobile.group.tlucontact

import android.os.Bundle
import android.widget.Toast
import com.mobile.group.tlucontact.fragment.ContactListFragment
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ContactListFragment())
                .commit()
        }

        navItemSelected()
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

    private fun navItemSelected() {
        val navView = findViewById<NavigationView>(R.id.sidebar_nav) ?: return
        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true

            // Thay đổi fragment (xử lý sau)

            true
        }
    }
}
