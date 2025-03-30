package com.mobile.group.tlucontact.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.fragment.StaffDetailFragment

class StaffActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_detail)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, StaffDetailFragment())
            .commit()
    }

    fun goBack(view: View) {
        finish()
    }
}