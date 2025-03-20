package com.mobile.group.tlucontact.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.fragment.UnitDetailFragment

class UnitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_detail)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, UnitDetailFragment()) // Load Fragment vào container
            .commit()
    }
}