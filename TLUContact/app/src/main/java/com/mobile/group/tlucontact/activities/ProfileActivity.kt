package com.mobile.group.tlucontact.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.fragment.ProfileFragment
import com.mobile.group.tlucontact.fragment.StaffDetailFragment

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ProfileFragment()) // Load Fragment vào container
            .commit()
    }

    fun goBack(view: View) {
        finish()
    }
}